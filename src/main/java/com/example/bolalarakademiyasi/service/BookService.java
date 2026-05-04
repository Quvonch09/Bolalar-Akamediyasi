package com.example.bolalarakademiyasi.service;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.PageBlockResponse;
import com.example.bolalarakademiyasi.dto.file.FileUploadResponse;
import com.example.bolalarakademiyasi.dto.request.ReqBook;
import com.example.bolalarakademiyasi.dto.response.ResBook;
import com.example.bolalarakademiyasi.dto.response.ResBookPage;
import com.example.bolalarakademiyasi.entity.Book;
import com.example.bolalarakademiyasi.entity.BookPage;
import com.example.bolalarakademiyasi.entity.Lesson;
import com.example.bolalarakademiyasi.entity.PageBlock;
import com.example.bolalarakademiyasi.entity.Subject;
import com.example.bolalarakademiyasi.entity.enums.BookStatus;
import com.example.bolalarakademiyasi.entity.enums.PageBlockType;
import com.example.bolalarakademiyasi.exception.BadRequestException;
import com.example.bolalarakademiyasi.exception.DataNotFoundException;
import com.example.bolalarakademiyasi.mapper.BookMapper;
import com.example.bolalarakademiyasi.repository.BookPageRepository;
import com.example.bolalarakademiyasi.repository.BookRepository;
import com.example.bolalarakademiyasi.repository.LessonRepository;
import com.example.bolalarakademiyasi.repository.PageBlockRepository;
import com.example.bolalarakademiyasi.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.filter.MissingImageReaderException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BookService {

    private static final float RENDER_DPI = 140F;

    private final BookRepository bookRepository;
    private final BookPageRepository bookPageRepository;
    private final PageBlockRepository pageBlockRepository;
    private final BookMapper bookMapper;
    private final LessonRepository lessonRepository;
    private final SubjectRepository subjectRepository;
    private final FileService fileService;

    @Transactional
    public ApiResponse<String> createBook(ReqBook reqBook) {
        validatePdf(reqBook.getFile(), true);

        Book book = new Book();
        applyMetadata(book, reqBook);

        Path tempDirectory = createTempBookDirectory();
        Path pdfPath = tempDirectory.resolve("source.pdf");

        try {
            copyMultipartToPath(reqBook.getFile(), pdfPath);
            populateCloudFileData(book, reqBook.getFile(), pdfPath);
            bookRepository.save(book);
            return ApiResponse.success(null, "Success");
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to process uploaded book file", ex);
        } finally {
            deleteDirectoryQuietly(tempDirectory);
        }
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<ResBook>> listBooks() {
        List<ResBook> list = bookRepository.findAll()
                .stream()
                .sorted((left, right) -> right.getCreatedAt().compareTo(left.getCreatedAt()))
                .map(bookMapper::resBookDto)
                .toList();
        return ApiResponse.success(list, "Success");
    }

    @Transactional(readOnly = true)
    public ApiResponse<ResBook> getBook(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Book not found"));
        return ApiResponse.success(bookMapper.resBook(book), "Success");
    }

    @Transactional
    public ApiResponse<String> updateBook(UUID id, ReqBook reqBook) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Book not found"));

        applyMetadata(book, reqBook);

        MultipartFile file = reqBook.getFile();
        if (file == null || file.isEmpty()) {
            bookRepository.save(book);
            return ApiResponse.success(null, "Success");
        }

        validatePdf(file, false);

        Set<String> oldUrls = collectManagedUrls(book);
        Path tempDirectory = createTempBookDirectory();
        Path pdfPath = tempDirectory.resolve("source.pdf");

        try {
            copyMultipartToPath(file, pdfPath);
            populateCloudFileData(book, file, pdfPath);
            bookRepository.save(book);
            deleteCloudFiles(oldUrls);
            return ApiResponse.success(null, "Success");
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to process uploaded book file", ex);
        } finally {
            deleteDirectoryQuietly(tempDirectory);
        }
    }

    @Transactional
    public ApiResponse<Void> deleteBook(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Book not found"));

        Set<String> urlsToDelete = collectManagedUrls(book);
        bookRepository.delete(book);
        deleteCloudFiles(urlsToDelete);
        return ApiResponse.success(null, "Success");
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<ResBookPage>> getPages(UUID bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new DataNotFoundException("Book not found");
        }
        List<ResBookPage> bookPageList = bookPageRepository.findByBookIdOrderByPageNumberAsc(bookId)
                .stream()
                .map(bookMapper::resBookPage)
                .toList();
        return ApiResponse.success(bookPageList, "Success");
    }

    @Transactional
    public ApiResponse<String> createPageBlock(UUID bookId, UUID pageId, PageBlockDraft draft) {
        BookPage page = bookPageRepository.findByIdAndBookId(pageId, bookId)
                .orElseThrow(() -> new IllegalArgumentException("Page not found for this book"));

        PageBlock block = new PageBlock();
        block.setPage(page);
        block.setType(draft.type());
        block.setTitle(draft.title().trim());
        block.setBody(draft.body());
        block.setImageUrl(draft.imageUrl());
        block.setX(normalize(draft.x(), "x"));
        block.setY(normalize(draft.y(), "y"));
        block.setWidth(normalizeSize(draft.width(), "width"));
        block.setHeight(normalizeSize(draft.height(), "height"));

        return ApiResponse.success(null, "Success");
    }

    @Transactional
    public ApiResponse<PageBlockResponse> updatePageBlockPosition(UUID bookId, UUID pageId, UUID blockId, Double x, Double y) {
        BookPage page = bookPageRepository.findByIdAndBookId(pageId, bookId)
                .orElseThrow(() -> new IllegalArgumentException("Page not found for this book"));

        PageBlock block = page.getBlocks().stream()
                .filter(item -> item.getId().equals(blockId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Block not found for this page"));

        block.setX(normalize(x, "x"));
        block.setY(normalize(y, "y"));
        return ApiResponse.success(bookMapper.toBlockResponse(pageBlockRepository.save(block)), "Success");
    }

    private void applyMetadata(Book book, ReqBook reqBook) {
        if (reqBook.getTitle() == null || reqBook.getTitle().isBlank()) {
            throw new BadRequestException("Title is required");
        }
        if (reqBook.getSubjectId() == null) {
            throw new BadRequestException("Subject is required");
        }
        if (reqBook.getLessonId() == null) {
            throw new BadRequestException("Lesson is required");
        }

        Subject subject = subjectRepository.findById(reqBook.getSubjectId()).orElseThrow(
                () -> new DataNotFoundException("Subject not found")
        );

        Lesson lesson = lessonRepository.findById(reqBook.getLessonId()).orElseThrow(
                () -> new DataNotFoundException("Lesson not found")
        );

        book.setTitle(reqBook.getTitle().trim());
        book.setDescription(reqBook.getDescription());
        book.setGradeLevel(reqBook.getGradeLevel());
        book.setSubject(subject);
        book.setLesson(lesson);
    }

    private void populateCloudFileData(Book book, MultipartFile file, Path pdfPath) throws IOException {
        FileUploadResponse bookUpload = fileService.uploadFile(file, "books/files");
        String bookUrl = requirePublicUrl(bookUpload, "book file");

        book.setFileUrl(bookUrl);
        book.setOriginalFileName(resolveOriginalPdfName(file));
        book.setStatus(BookStatus.PROCESSING);

        replacePagesFromPdf(book, pdfPath);
        book.setCoverImageUrl(book.getPages().isEmpty() ? null : book.getPages().get(0).getImageUrl());
        book.setStatus(BookStatus.READY);
    }

    private void replacePagesFromPdf(Book book, Path pdfPath) throws IOException {
        book.getPages().clear();

        try (PDDocument document = Loader.loadPDF(pdfPath.toFile())) {
            PDFRenderer renderer = new PDFRenderer(document);

            for (int index = 0; index < document.getNumberOfPages(); index++) {
                BufferedImage image = renderer.renderImageWithDPI(index, RENDER_DPI, ImageType.RGB);

                BookPage page = new BookPage();
                page.setBook(book);
                page.setPageNumber(index + 1);
                page.setImageUrl(uploadRenderedPage(image, index + 1));
                page.setWidth(image.getWidth());
                page.setHeight(image.getHeight());
                book.getPages().add(page);
            }

            book.setTotalPages(book.getPages().size());
        } catch (MissingImageReaderException ex) {
            book.setStatus(BookStatus.FAILED);
            throw new IllegalStateException(
                    "PDF contains an image format that needs an extra decoder. JBIG2 support was missing while rendering this file.",
                    ex
            );
        } catch (IOException ex) {
            book.setStatus(BookStatus.FAILED);
            throw ex;
        } catch (Exception ex) {
            book.setStatus(BookStatus.FAILED);
            throw new IllegalStateException("Failed to render PDF pages", ex);
        }
    }

    private String uploadRenderedPage(BufferedImage image, int pageNumber) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", outputStream);
            MultipartFile multipartFile = new MockMultipartFile(
                    "file",
                    "page-" + String.format("%03d", pageNumber) + ".png",
                    "image/png",
                    outputStream.toByteArray()
            );
            FileUploadResponse uploadResponse = fileService.uploadFile(multipartFile, "books/pages");
            return requirePublicUrl(uploadResponse, "book page image");
        }
    }

    private String requirePublicUrl(FileUploadResponse uploadResponse, String fileType) {
        if (uploadResponse.url() == null || uploadResponse.url().isBlank()) {
            throw new IllegalStateException("Cloudflare R2 public URL is not available for " + fileType);
        }
        return uploadResponse.url();
    }

    private void validatePdf(MultipartFile file, boolean required) {
        if (file == null || file.isEmpty()) {
            if (required) {
                throw new BadRequestException("PDF file is required");
            }
            return;
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.toLowerCase().endsWith(".pdf")) {
            throw new BadRequestException("Only PDF files are supported");
        }
    }

    private String resolveOriginalPdfName(MultipartFile file) {
        String originalName = file.getOriginalFilename();
        return (originalName == null || originalName.isBlank()) ? "book.pdf" : originalName;
    }

    private void copyMultipartToPath(MultipartFile file, Path destination) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private Path createTempBookDirectory() {
        try {
            return Files.createTempDirectory("book-" + UUID.randomUUID() + "-");
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to prepare temporary book directory", ex);
        }
    }

    private void deleteDirectoryQuietly(Path root) {
        if (root == null || Files.notExists(root)) {
            return;
        }
        try (Stream<Path> stream = Files.walk(root)) {
            stream.sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException ignored) {
                }
            });
        } catch (IOException ignored) {
        }
    }

    private Set<String> collectManagedUrls(Book book) {
        Set<String> urls = new LinkedHashSet<>();
        if (book.getFileUrl() != null && !book.getFileUrl().isBlank()) {
            urls.add(book.getFileUrl());
        }
        if (book.getCoverImageUrl() != null && !book.getCoverImageUrl().isBlank()) {
            urls.add(book.getCoverImageUrl());
        }
        for (BookPage page : book.getPages()) {
            if (page.getImageUrl() != null && !page.getImageUrl().isBlank()) {
                urls.add(page.getImageUrl());
            }
        }
        return urls;
    }

    private void deleteCloudFiles(Set<String> urls) {
        for (String url : urls) {
            try {
                fileService.deleteFileByUrl(url);
            } catch (Exception ignored) {
            }
        }
    }

    private double normalize(Double value, String fieldName) {
        if (value == null || value < 0 || value > 100) {
            throw new IllegalArgumentException(fieldName + " must be between 0 and 100");
        }
        return value;
    }

    private double normalizeSize(Double value, String fieldName) {
        if (value == null || value <= 0 || value > 100) {
            throw new IllegalArgumentException(fieldName + " must be between 0 and 100");
        }
        return value;
    }

    public record PageBlockDraft(
            PageBlockType type,
            String title,
            String body,
            String imageUrl,
            Double x,
            Double y,
            Double width,
            Double height
    ) {
    }
}
