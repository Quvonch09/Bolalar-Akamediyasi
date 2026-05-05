package com.example.bolalarakademiyasi.controller;
import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.request.ReqLesson;
import com.example.bolalarakademiyasi.dto.response.ResLesson;
import com.example.bolalarakademiyasi.dto.response.ResPageable;
import com.example.bolalarakademiyasi.entity.Lesson;
import com.example.bolalarakademiyasi.service.LessonService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lessons")
public class LessonController {


    private final LessonService lessonService;


    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_TEACHER')")
    public ResponseEntity<ApiResponse<String>> save (@RequestBody ReqLesson reqLesson) {
        return ResponseEntity.ok(lessonService.saveLesson(reqLesson));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_TEACHER')")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(lessonService.deleteLesson(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResLesson>> getLesson(@PathVariable UUID id) {
        return ResponseEntity.ok(lessonService.getOneLesson(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_TEACHER')")
    public ResponseEntity<ApiResponse<String>>  updateLesson(@PathVariable UUID id, @RequestBody ReqLesson reqLesson) {
        return ResponseEntity.ok(lessonService.updateLesson(id, reqLesson));
    }


    @GetMapping("/search")
    public ResponseEntity<ApiResponse<ResPageable>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String subjectName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(lessonService.searchLesson(name, subjectName, page, size));
    }


}
