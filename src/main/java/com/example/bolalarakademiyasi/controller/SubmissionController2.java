package com.example.bolalarakademiyasi.controller;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.request.ReqSubmission;
import com.example.bolalarakademiyasi.dto.response.ResPageable;
import com.example.bolalarakademiyasi.dto.response.ResSubmission;
import com.example.bolalarakademiyasi.service.SubmissionService2;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController2 {

        private final SubmissionService2 submissionService2;

        @PostMapping
        @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_USER')") // Asosan o'quvchilar yuboradi
        @Operation(description = "O'quvchi tomonidan bajarilgan vazifani yuborish")
        public ResponseEntity<ApiResponse<String>> save(@Valid @RequestBody ReqSubmission reqSubmission) {
            return ResponseEntity.ok(submissionService2.saveSubmission(reqSubmission));
        }

//        @PutMapping("/{id}/grade")
//        @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_TEACHER')") // Siz yozgan rollar kabi[cite: 3]
//        @Operation(description = "O'qituvchi tomonidan vazifani tekshirish va baholash (1 dan 10 gacha)")
//        public ResponseEntity<ApiResponse<String>> gradeSubmission(
//                @PathVariable UUID id,
//                @Valid @RequestBody ReqGradeSubmission reqGrade) {
//            return ResponseEntity.ok(submissionService.gradeSubmission(id, reqGrade));
//        }


        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<ResSubmission>> getOneSubmission(@PathVariable UUID id) {
            return ResponseEntity.ok(submissionService2.getOneSubmission(id));
        }

        @GetMapping("/homework/{homeworkId}")
        @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_TEACHER')") //[cite: 3]
        @Operation(description = "Bitta uy vazifasiga (Homework) tegishli barcha javoblarni ro'yxat qilib olish")
        public ResponseEntity<ApiResponse<ResPageable>> getSubmissionsByHomework(
                @PathVariable UUID homeworkId,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size) {
            return ResponseEntity.ok(submissionService2.getSubmissionsByHomework(homeworkId, page, size));
        }


        @DeleteMapping("/{id}")
        @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_STUDENT')") //[cite: 3]
        public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {
            return ResponseEntity.ok(submissionService2.deleteSubmission(id));
        }
    }
