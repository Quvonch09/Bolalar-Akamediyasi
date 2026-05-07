package com.example.bolalarakademiyasi.service;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.request.ReqVideoLesson;
import com.example.bolalarakademiyasi.dto.request.ReqVideoLessonPayload;
import com.example.bolalarakademiyasi.dto.response.ResPageable;
import com.example.bolalarakademiyasi.dto.response.ResVideoLesson;
import com.example.bolalarakademiyasi.entity.Lesson;
import com.example.bolalarakademiyasi.entity.VideoLesson;
import com.example.bolalarakademiyasi.exception.BadRequestException;
import com.example.bolalarakademiyasi.exception.DataNotFoundException;
import com.example.bolalarakademiyasi.mapper.VideoLessonMapper;
import com.example.bolalarakademiyasi.repository.LessonRepository;
import com.example.bolalarakademiyasi.repository.VideoLessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoLessonService {

    private final VideoLessonRepository videoLessonRepository;
    private final LessonRepository lessonRepository;
    private final VideoLessonMapper videoLessonMapper;

    public ApiResponse<String> saveVideoLesson(ReqVideoLesson req) {
        Lesson lesson = lessonRepository.findByIdAndActiveTrue(req.getLessonId()).orElseThrow(
                () -> new DataNotFoundException("Lesson not found")
        );

        if (!hasVideoDetails(req)) {
            throw new BadRequestException("VideoLesson yaratish uchun kamida bitta video ma'lumot yuborilishi kerak");
        }

        if (videoLessonRepository.existsByLessonIdAndActiveTrue(lesson.getId())) {
            throw new BadRequestException("Bu lesson uchun VideoLesson allaqachon mavjud");
        }

        VideoLesson videoLesson = VideoLesson.builder()
                .lesson(lesson)
                .title(req.getTitle())
                .videoUrl(req.getVideoUrl())
                .duration(req.getDuration())
                .fileSize(req.getFileSize())
                .format(req.getFormat())
                .build();

        videoLessonRepository.save(videoLesson);
        return ApiResponse.success(null, "Video lesson saved successfully");
    }

    public ApiResponse<String> updateVideoLesson(UUID id, ReqVideoLesson req) {
        VideoLesson videoLesson = videoLessonRepository.findByIdAndActiveTrue(id).orElseThrow(
                () -> new DataNotFoundException("VideoLesson not found")
        );

        Lesson lesson = lessonRepository.findByIdAndActiveTrue(req.getLessonId()).orElseThrow(
                () -> new DataNotFoundException("Lesson not found")
        );

        videoLessonRepository.findByLessonIdAndActiveTrue(lesson.getId())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new BadRequestException("Bu lesson boshqa VideoLesson bilan band");
                });

        if (!hasVideoDetails(req)) {
            throw new BadRequestException("VideoLesson update uchun kamida bitta video ma'lumot yuborilishi kerak");
        }

        videoLesson.setLesson(lesson);
        videoLesson.setTitle(req.getTitle());
        videoLesson.setVideoUrl(req.getVideoUrl());
        videoLesson.setDuration(req.getDuration());
        videoLesson.setFileSize(req.getFileSize());
        videoLesson.setFormat(req.getFormat());

        videoLessonRepository.save(videoLesson);
        return ApiResponse.success(null, "Video lesson updated successfully");
    }

    public ApiResponse<String> deleteVideoLesson(UUID id) {
        VideoLesson videoLesson = videoLessonRepository.findByIdAndActiveTrue(id).orElseThrow(
                () -> new DataNotFoundException("VideoLesson not found")
        );

        videoLesson.softDelete();
        videoLessonRepository.save(videoLesson);
        return ApiResponse.success(null, "Video lesson deleted successfully");
    }

    public ApiResponse<ResVideoLesson> getOneVideoLesson(UUID id) {
        VideoLesson videoLesson = videoLessonRepository.findByIdAndActiveTrue(id).orElseThrow(
                () -> new DataNotFoundException("VideoLesson not found")
        );
        return ApiResponse.success(videoLessonMapper.toResponse(videoLesson), "Success");
    }

    public ApiResponse<ResVideoLesson> getByLessonId(UUID lessonId) {
        lessonRepository.findByIdAndActiveTrue(lessonId).orElseThrow(
                () -> new DataNotFoundException("Lesson not found")
        );

        VideoLesson videoLesson = videoLessonRepository.findByLessonIdAndActiveTrue(lessonId).orElseThrow(
                () -> new DataNotFoundException("VideoLesson not found")
        );

        return ApiResponse.success(videoLessonMapper.toResponse(videoLesson), "Success");
    }

    public ApiResponse<ResPageable> searchVideoLessons(String title, String lessonName, String subjectName, int page, int size) {
        Page<VideoLesson> videoLessonPage = videoLessonRepository.searchVideoLessons(
                toLikePattern(title),
                toLikePattern(lessonName),
                toLikePattern(subjectName),
                PageRequest.of(page, size)
        );

        if (videoLessonPage.isEmpty()) {
            throw new DataNotFoundException("Video lessons not found");
        }

        List<ResVideoLesson> list = videoLessonPage.getContent().stream()
                .map(videoLessonMapper::toResponse)
                .toList();

        ResPageable resPageable = ResPageable.builder()
                .page(page)
                .size(size)
                .totalElements(videoLessonPage.getTotalElements())
                .totalPage(videoLessonPage.getTotalPages())
                .body(list)
                .build();

        return ApiResponse.success(resPageable, "Success");
    }

    public VideoLesson createIfHasDetails(Lesson lesson, ReqVideoLessonPayload payload) {
        if (payload == null || !hasVideoDetails(payload)) {
            return null;
        }

        VideoLesson videoLesson = VideoLesson.builder()
                .lesson(lesson)
                .title(payload.getTitle())
                .videoUrl(payload.getVideoUrl())
                .duration(payload.getDuration())
                .fileSize(payload.getFileSize())
                .format(payload.getFormat())
                .build();

        return videoLessonRepository.save(videoLesson);
    }

    public VideoLesson createOrUpdateByLesson(Lesson lesson, ReqVideoLessonPayload payload) {
        if (payload == null || !hasVideoDetails(payload)) {
            return null;
        }

        VideoLesson videoLesson = videoLessonRepository.findByLessonIdAndActiveTrue(lesson.getId())
                .orElse(VideoLesson.builder().lesson(lesson).build());

        videoLesson.setLesson(lesson);
        videoLesson.setTitle(payload.getTitle());
        videoLesson.setVideoUrl(payload.getVideoUrl());
        videoLesson.setDuration(payload.getDuration());
        videoLesson.setFileSize(payload.getFileSize());
        videoLesson.setFormat(payload.getFormat());

        return videoLessonRepository.save(videoLesson);
    }

    private boolean hasVideoDetails(ReqVideoLesson req) {
        return hasText(req.getTitle())
                || hasText(req.getVideoUrl())
                || req.getDuration() != null
                || req.getFileSize() != null
                || hasText(req.getFormat());
    }

    private boolean hasVideoDetails(ReqVideoLessonPayload payload) {
        return hasText(payload.getTitle())
                || hasText(payload.getVideoUrl())
                || payload.getDuration() != null
                || payload.getFileSize() != null
                || hasText(payload.getFormat());
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private String toLikePattern(String value) {
        return hasText(value) ? "%" + value.trim().toLowerCase() + "%" : null;
    }
}
