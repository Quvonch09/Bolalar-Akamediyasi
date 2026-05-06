package com.example.bolalarakademiyasi.controller;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.request.ReqGradeSubmission;
import com.example.bolalarakademiyasi.dto.request.ReqSubmission;
import com.example.bolalarakademiyasi.dto.request.ReqUpdateSubmission;
import com.example.bolalarakademiyasi.dto.response.ResPageable;
import com.example.bolalarakademiyasi.dto.response.ResSubmission;
import com.example.bolalarakademiyasi.service.SubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;




    @PostMapping
    public ResponseEntity<ApiResponse<String>> saveSubmission(@RequestBody ReqSubmission req) {
        return ResponseEntity.ok(submissionService.saveSubmission(req));
    }

    @PutMapping("/{id}/grade")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @Operation(description = "Bu API Faqat Teacher Baholashi uchun ")
    public ResponseEntity<ApiResponse<String>> gradeSubmission(
            @PathVariable UUID id, 
            @RequestBody ReqGradeSubmission req) {
        return ResponseEntity.ok(submissionService.gradeSubmission(id, req));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    @Operation(description = "Bu API Faqat Admin uchun ")
    public ResponseEntity<ApiResponse<ResPageable>> getAllSubmissions(
            @RequestParam(required = false) String studentName,
            @RequestParam(required = false) String homeworkTitle,
            @RequestParam(required = false) Boolean isGraded,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(submissionService.getAllSubmissions(
                studentName, homeworkTitle, isGraded, page, size
        ));
    }



    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateSubmission(
            @PathVariable UUID id, 
            @RequestBody ReqUpdateSubmission req) {
        return ResponseEntity.ok(submissionService.updateSubmission(id, req));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteSubmission(@PathVariable UUID id) {
        return ResponseEntity.ok(submissionService.deleteSubmission(id));
    }




    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResSubmission>> getOneSubmission(@PathVariable UUID id) {
        return ResponseEntity.ok(submissionService.getOneSubmission(id));
    }


}