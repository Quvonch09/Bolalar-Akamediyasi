package com.example.bolalarakademiyasi.controller;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.request.ReqVideoProgress;
import com.example.bolalarakademiyasi.dto.response.ResPageable;
import com.example.bolalarakademiyasi.dto.response.ResVideoProgress;
import com.example.bolalarakademiyasi.service.VideoProgressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/video-progress")
@RequiredArgsConstructor
public class VideoProgressController {

    private final VideoProgressService videoProgressService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_STUDENT')")
    public ResponseEntity<ApiResponse<String>> save(@Valid @RequestBody ReqVideoProgress req) {
        return ResponseEntity.ok(videoProgressService.saveVideoProgress(req));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_STUDENT')")
    public ResponseEntity<ApiResponse<String>> update(@PathVariable UUID id, @Valid @RequestBody ReqVideoProgress req) {
        return ResponseEntity.ok(videoProgressService.updateVideoProgress(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_TEACHER')")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(videoProgressService.deleteVideoProgress(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResVideoProgress>> getOne(@PathVariable UUID id) {
        return ResponseEntity.ok(videoProgressService.getOneVideoProgress(id));
    }

    @GetMapping("/student/{studentId}/video-lesson/{videoLessonId}")
    public ResponseEntity<ApiResponse<ResVideoProgress>> getByStudentAndVideo(@PathVariable UUID studentId,
                                                                              @PathVariable UUID videoLessonId) {
        return ResponseEntity.ok(videoProgressService.getByStudentAndVideoLesson(studentId, videoLessonId));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<ResPageable>> search(@RequestParam(required = false) String studentName,
                                                           @RequestParam(required = false) String videoTitle,
                                                           @RequestParam(required = false) String lessonName,
                                                           @RequestParam(required = false) Boolean completed,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(videoProgressService.searchVideoProgresses(
                studentName, videoTitle, lessonName, completed, page, size
        ));
    }
}
