package com.example.bolalarakademiyasi.controller;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.request.ReqVideoLesson;
import com.example.bolalarakademiyasi.dto.response.ResPageable;
import com.example.bolalarakademiyasi.dto.response.ResVideoLesson;
import com.example.bolalarakademiyasi.service.VideoLessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/video-lessons")
@RequiredArgsConstructor
public class VideoLessonController {

    private final VideoLessonService videoLessonService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_TEACHER')")
    public ResponseEntity<ApiResponse<String>> save(@Valid @RequestBody ReqVideoLesson req) {
        return ResponseEntity.ok(videoLessonService.saveVideoLesson(req));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_TEACHER')")
    public ResponseEntity<ApiResponse<String>> update(@PathVariable UUID id, @Valid @RequestBody ReqVideoLesson req) {
        return ResponseEntity.ok(videoLessonService.updateVideoLesson(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_TEACHER')")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(videoLessonService.deleteVideoLesson(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResVideoLesson>> getOne(@PathVariable UUID id) {
        return ResponseEntity.ok(videoLessonService.getOneVideoLesson(id));
    }

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<ApiResponse<ResVideoLesson>> getByLesson(@PathVariable UUID lessonId) {
        return ResponseEntity.ok(videoLessonService.getByLessonId(lessonId));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<ResPageable>> search(@RequestParam(required = false) String title,
                                                           @RequestParam(required = false) String lessonName,
                                                           @RequestParam(required = false) String subjectName,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(videoLessonService.searchVideoLessons(title, lessonName, subjectName, page, size));
    }
}
