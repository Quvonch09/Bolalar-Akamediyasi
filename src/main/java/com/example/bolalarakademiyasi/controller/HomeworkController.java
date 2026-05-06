package com.example.bolalarakademiyasi.controller;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.request.ReqFeedback;
import com.example.bolalarakademiyasi.dto.request.ReqHomework;
import com.example.bolalarakademiyasi.dto.response.ResHomework;
import com.example.bolalarakademiyasi.dto.response.ResPageable;
import com.example.bolalarakademiyasi.service.HomeworkService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/homeworks")
@RequiredArgsConstructor
public class HomeworkController {

    private final HomeworkService homeworkService;



    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_TEACHER')")
    @Operation(description = " DeadlineEnum Type = HOUR, DAY, WEEK, MONTH")
    public ResponseEntity<ApiResponse<String>> save(@RequestBody ReqHomework reqHomework) {
        return ResponseEntity.ok(homeworkService.saveHomework(reqHomework));
    }



    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_TEACHER')")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(homeworkService.deleteHomework(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResHomework>> getOneHomework(@PathVariable UUID id) {
        return ResponseEntity.ok(homeworkService.getOneHomework(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_TEACHER')")
    public ResponseEntity<ApiResponse<String>> update(@PathVariable UUID id, @RequestBody ReqHomework reqHomework) {
        return ResponseEntity.ok(homeworkService.updateHomework(id, reqHomework));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<ResPageable>> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String deadLineEnum,
            @RequestParam(required = false) String lessonTitle,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(homeworkService.searchHomework(title, deadLineEnum, lessonTitle, page, size));
    }



}
