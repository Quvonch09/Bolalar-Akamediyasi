package com.example.bolalarakademiyasi.service;

import com.example.bolalarakademiyasi.configuration.StorageProperties;
import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.BookPageResponse;
import com.example.bolalarakademiyasi.dto.BookResponse;
import com.example.bolalarakademiyasi.dto.PageBlockResponse;
import com.example.bolalarakademiyasi.dto.request.ReqBook;
import com.example.bolalarakademiyasi.dto.response.ResBook;
import com.example.bolalarakademiyasi.dto.response.ResBookPage;
import com.example.bolalarakademiyasi.entity.*;
import com.example.bolalarakademiyasi.entity.enums.BookStatus;
import com.example.bolalarakademiyasi.entity.enums.PageBlockType;
import com.example.bolalarakademiyasi.exception.DataNotFoundException;
import com.example.bolalarakademiyasi.mapper.BookMapper;
import com.example.bolalarakademiyasi.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.filter.MissingImageReaderException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {

    private static final float RENDER_DPI = 140F;

    private final BookRepository bookRepository;
    private final BookPageRepository bookPageRepository;
    private final PageBlockRepository pageBlockRepository;
    private final StorageProperties storageProperties;
    private final LessonRepository lessonRepository;
    private final SubjectRepository subjectRepository;
    private final BookMapper bookMapper;


    @Transactional
    public ApiResponse<String> createBook(ReqBook reqBook) {
        validatePdf(reqBook.getFile());
        Path root = Path.of(storageProperties.root()).toAbsolutePath().normalize();
        UUID folderId = UUID.randomUUID();
        Path bookFolder = root.resolve("books").resolve(folderId.toString());
        Path pagesFolder = bookFolder.resolve("pages");
        Path pdfPath = bookFolder.resolve("source.pdf");

        try {
            Files.createDirectories(pagesFolder);
            try (InputStream inputStream = reqBook.getFile().getInputStream()) {
                Files.copy(inputStream, pdfPath, StandardCopyOption.REPLACE_EXISTING);
            }

            Lesson lesson = lessonRepository.findById(reqBook.getLessonId()).orElseThrow(
                    () -> new DataNotFoundException("Lesson not found!")
            );

            Subject subject = subjectRepository.findById(reqBook.getSubjectId()).orElseThrow(
                    () -> new DataNotFoundException("Subject not found!")
            );

            Book book = new Book();
            book.setTitle((reqBook.getTitle() == null || reqBook.getTitle().isBlank()) ? reqBook.getFile().getOriginalFilename() : reqBook.getTitle().trim());
            book.setDescription(reqBook.getDescription());
            book.setOriginalFileName(reqBook.getFile().getOriginalFilename());
            book.setPdfPath(root.relativize(pdfPath).toString());
            book.setStatus(BookStatus.PROCESSING);
            book.setTotalPages(0);
            book.setGradeLevel(reqBook.getGradeLevel());
            book.setLesson(lesson);
            book.setSubject(subject);
            book = bookRepository.save(book);

            renderBookPages(book, pdfPath, pagesFolder, root);
            bookRepository.save(book);
            return ApiResponse.success(null, "Successfully Created Book");
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to store uploaded PDF", ex);
        }
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<ResBook>> listBooks() {
        List<ResBook> list = bookRepository.findAll()
                .stream()
                .sorted((left, right) -> right.getCreatedAt().compareTo(left.getCreatedAt()))
                .map(bookMapper::resBook)
                .toList();
        return ApiResponse.success(list, "Successfully retrieved Books");
    }

    @Transactional(readOnly = true)
    public ApiResponse<BookResponse> getBook(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Book not found!"));
        List<BookPage> bookPageList = bookPageRepository.
                findByBookIdOrderByPageNumberAsc(book.getId());
        return ApiResponse.success(bookMapper.toResponse(book, bookPageList), "Successfully retrieved Book");
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<BookPageResponse>> getPages(UUID bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new DataNotFoundException("Book not found!"));
        List<BookPageResponse> bookPageList = bookPageRepository.
                findByBookIdOrderByPageNumberAsc(book.getId()).stream().map(bookMapper::toPageResponse).toList();
        return ApiResponse.success(bookPageList, "Successfully retrieved Book Pages");
    }

    @Transactional
    public ApiResponse<PageBlockResponse> createPageBlock(UUID bookId, UUID pageId, PageBlockDraft draft) {
        BookPage page = bookPageRepository.findByIdAndBookId(pageId, bookId)
                .orElseThrow(() -> new DataNotFoundException("Page not found for this book"));

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
        PageBlock save = pageBlockRepository.save(block);
        return ApiResponse.success(bookMapper.toBlockResponse(save), "Successfully Created Page Block");
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
        PageBlock save = pageBlockRepository.save(block);
        return ApiResponse.success(bookMapper.toBlockResponse(save), "Successfully Updated Page Block");
    }

    private void renderBookPages(Book book, Path pdfPath, Path pagesFolder, Path root) {
        try (PDDocument document = Loader.loadPDF(pdfPath.toFile())) {
            PDFRenderer renderer = new PDFRenderer(document);
            for (int index = 0; index < document.getNumberOfPages(); index++) {
                BufferedImage image = renderer.renderImageWithDPI(index, RENDER_DPI, ImageType.RGB);
                String fileName = "page-" + String.format("%03d", index + 1) + ".png";
                Path imagePath = pagesFolder.resolve(fileName);
                ImageIO.write(image, "png", imagePath.toFile());

                BookPage page = new BookPage();
                page.setBook(book);
                page.setPageNumber(index + 1);
                page.setImagePath(root.relativize(imagePath).toString());
                page.setWidth(image.getWidth());
                page.setHeight(image.getHeight());
                book.getPages().add(page);
            }

            if (!book.getPages().isEmpty()) {
                book.setCoverImagePath(book.getPages().get(0).getImagePath());
            }
            book.setTotalPages(book.getPages().size());
            book.setStatus(BookStatus.READY);
        } catch (MissingImageReaderException ex) {
            book.setStatus(BookStatus.FAILED);
            throw new IllegalStateException(
                    "PDF contains an image format that needs an extra decoder. JBIG2 support was missing while rendering this file.",
                    ex
            );
        } catch (Exception ex) {
            book.setStatus(BookStatus.FAILED);
            throw new IllegalStateException("Failed to render PDF pages", ex);
        }
    }

    private void validatePdf(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("PDF file is required");
        }
        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Only PDF files are supported");
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
