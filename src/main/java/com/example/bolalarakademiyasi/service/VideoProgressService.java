package com.example.bolalarakademiyasi.service;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.request.ReqVideoProgress;
import com.example.bolalarakademiyasi.dto.response.ResPageable;
import com.example.bolalarakademiyasi.dto.response.ResVideoProgress;
import com.example.bolalarakademiyasi.entity.Student;
import com.example.bolalarakademiyasi.entity.VideoLesson;
import com.example.bolalarakademiyasi.entity.VideoProgress;
import com.example.bolalarakademiyasi.exception.BadRequestException;
import com.example.bolalarakademiyasi.exception.DataNotFoundException;
import com.example.bolalarakademiyasi.mapper.VideoProgressMapper;
import com.example.bolalarakademiyasi.repository.StudentRepository;
import com.example.bolalarakademiyasi.repository.VideoLessonRepository;
import com.example.bolalarakademiyasi.repository.VideoProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoProgressService {

    private final VideoProgressRepository videoProgressRepository;
    private final StudentRepository studentRepository;
    private final VideoLessonRepository videoLessonRepository;
    private final VideoProgressMapper videoProgressMapper;

    public ApiResponse<String> saveVideoProgress(ReqVideoProgress req) {
        validateProgress(req);

        if (videoProgressRepository.findByStudentIdAndVideoLessonIdAndActiveTrue(req.getStudentId(), req.getVideoLessonId()).isPresent()) {
            throw new BadRequestException("Bu student uchun ushbu video progress allaqachon mavjud");
        }

        Student student = getStudent(req.getStudentId());
        VideoLesson videoLesson = getVideoLesson(req.getVideoLessonId());

        VideoProgress videoProgress = VideoProgress.builder()
                .student(student)
                .videoLesson(videoLesson)
                .watchedSeconds(req.getWatchedSeconds())
                .lastPositionSeconds(resolveLastPosition(req, videoLesson))
                .completed(resolveCompleted(req, videoLesson))
                .lastWatchedAt(LocalDateTime.now())
                .build();

        videoProgressRepository.save(videoProgress);
        return ApiResponse.success(null, "Video progress saved successfully");
    }

    public ApiResponse<String> updateVideoProgress(UUID id, ReqVideoProgress req) {
        validateProgress(req);

        VideoProgress videoProgress = videoProgressRepository.findByIdAndActiveTrue(id).orElseThrow(
                () -> new DataNotFoundException("VideoProgress not found")
        );

        Student student = getStudent(req.getStudentId());
        VideoLesson videoLesson = getVideoLesson(req.getVideoLessonId());

        videoProgressRepository.findByStudentIdAndVideoLessonIdAndActiveTrue(req.getStudentId(), req.getVideoLessonId())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new BadRequestException("Bu student uchun ushbu video progress allaqachon mavjud");
                });

        videoProgress.setStudent(student);
        videoProgress.setVideoLesson(videoLesson);
        videoProgress.setWatchedSeconds(req.getWatchedSeconds());
        videoProgress.setLastPositionSeconds(resolveLastPosition(req, videoLesson));
        videoProgress.setCompleted(resolveCompleted(req, videoLesson));
        videoProgress.setLastWatchedAt(LocalDateTime.now());

        videoProgressRepository.save(videoProgress);
        return ApiResponse.success(null, "Video progress updated successfully");
    }

    public ApiResponse<String> deleteVideoProgress(UUID id) {
        VideoProgress videoProgress = videoProgressRepository.findByIdAndActiveTrue(id).orElseThrow(
                () -> new DataNotFoundException("VideoProgress not found")
        );

        videoProgress.softDelete();
        videoProgressRepository.save(videoProgress);
        return ApiResponse.success(null, "Video progress deleted successfully");
    }

    public ApiResponse<ResVideoProgress> getOneVideoProgress(UUID id) {
        VideoProgress videoProgress = videoProgressRepository.findByIdAndActiveTrue(id).orElseThrow(
                () -> new DataNotFoundException("VideoProgress not found")
        );

        return ApiResponse.success(videoProgressMapper.toResponse(videoProgress), "Success");
    }

    public ApiResponse<ResVideoProgress> getByStudentAndVideoLesson(UUID studentId, UUID videoLessonId) {
        getStudent(studentId);
        getVideoLesson(videoLessonId);

        VideoProgress videoProgress = videoProgressRepository.findByStudentIdAndVideoLessonIdAndActiveTrue(studentId, videoLessonId).orElseThrow(
                () -> new DataNotFoundException("VideoProgress not found")
        );

        return ApiResponse.success(videoProgressMapper.toResponse(videoProgress), "Success");
    }

    public ApiResponse<ResPageable> searchVideoProgresses(String studentName,
                                                          String videoTitle,
                                                          String lessonName,
                                                          Boolean completed,
                                                          int page,
                                                          int size) {
        Page<VideoProgress> progressPage = videoProgressRepository.searchVideoProgresses(
                toLikePattern(studentName),
                toLikePattern(videoTitle),
                toLikePattern(lessonName),
                completed,
                PageRequest.of(page, size)
        );

        if (progressPage.isEmpty()) {
            throw new DataNotFoundException("Video progresses not found");
        }

        List<ResVideoProgress> list = progressPage.getContent().stream()
                .map(videoProgressMapper::toResponse)
                .toList();

        ResPageable resPageable = ResPageable.builder()
                .page(page)
                .size(size)
                .totalElements(progressPage.getTotalElements())
                .totalPage(progressPage.getTotalPages())
                .body(list)
                .build();

        return ApiResponse.success(resPageable, "Success");
    }

    private Student getStudent(UUID studentId) {
        return studentRepository.findByIdAndActiveTrue(studentId).orElseThrow(
                () -> new DataNotFoundException("Student not found")
        );
    }

    private VideoLesson getVideoLesson(UUID videoLessonId) {
        return videoLessonRepository.findByIdAndActiveTrue(videoLessonId).orElseThrow(
                () -> new DataNotFoundException("VideoLesson not found")
        );
    }

    private void validateProgress(ReqVideoProgress req) {
        if (req.getWatchedSeconds() == null) {
            throw new BadRequestException("Watched seconds bo'sh bo'lishi mumkin emas");
        }
        if (req.getWatchedSeconds() < 0) {
            throw new BadRequestException("Watched seconds manfiy bo'lishi mumkin emas");
        }
        if (req.getLastPositionSeconds() != null && req.getLastPositionSeconds() < 0) {
            throw new BadRequestException("Last position seconds manfiy bo'lishi mumkin emas");
        }
    }

    private Integer resolveLastPosition(ReqVideoProgress req, VideoLesson videoLesson) {
        int lastPosition = req.getLastPositionSeconds() != null ? req.getLastPositionSeconds() : req.getWatchedSeconds();
        if (videoLesson.getDuration() == null) {
            return lastPosition;
        }
        return Math.min(lastPosition, videoLesson.getDuration());
    }

    private Boolean resolveCompleted(ReqVideoProgress req, VideoLesson videoLesson) {
        if (req.getCompleted() != null) {
            return req.getCompleted();
        }
        return videoLesson.getDuration() != null && req.getWatchedSeconds() >= videoLesson.getDuration();
    }

    private String toLikePattern(String value) {
        return value != null && !value.isBlank() ? "%" + value.trim().toLowerCase() + "%" : null;
    }
}
