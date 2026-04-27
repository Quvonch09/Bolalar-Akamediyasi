package com.example.bolalarakademiyasi.controller;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.request.ReqMark;
import com.example.bolalarakademiyasi.dto.request.ReqMarkDTO;
import com.example.bolalarakademiyasi.dto.response.ResMark;
import com.example.bolalarakademiyasi.dto.response.ResPageable;
import com.example.bolalarakademiyasi.entity.enums.PeriodFilter;
import com.example.bolalarakademiyasi.security.CustomUserDetails;
import com.example.bolalarakademiyasi.service.MarkService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/mark")
@RequiredArgsConstructor
public class MarkController {
    private final MarkService markService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public ResponseEntity<ApiResponse<String>> saveMark(@RequestBody ReqMark reqMark){
        return ResponseEntity.ok(markService.saveMark(reqMark));
    }



    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public ResponseEntity<ApiResponse<String>> updateMark(@RequestBody ReqMarkDTO reqMarkDTO){
        return ResponseEntity.ok(markService.updateMark(reqMarkDTO));
    }


    @DeleteMapping("/{markId}")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public ResponseEntity<ApiResponse<String>> deleteMark(@PathVariable UUID markId){
        return ResponseEntity.ok(markService.deleteMark(markId));
    }


    @GetMapping("/byGroup/{sinfId}")
    @Operation(summary = "Faqat admin uchun barcha marklarni kurish")
    public ResponseEntity<ApiResponse<ResPageable>> getForAdmin(@RequestParam(required = false) String keyword,
                                                                @PathVariable UUID sinfId,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(markService.getAllMarkForAdmin(keyword,sinfId, page, size));
    }


    @GetMapping("/myMarks")
    @Operation(summary = "Teacher, Student, Parent uziga tegishli baholarni kurish")
    @PreAuthorize("hasAnyRole('ROLE_TEACHER', 'ROLE_PARENT', 'STUDENT','ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ResPageable>> getMyMarks(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                               @RequestParam PeriodFilter filter,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(markService.getMyMarks(customUserDetails,filter, page, size));
    }


    @GetMapping("/{markId}")
    public ResponseEntity<ApiResponse<ResMark>> getONeMark(@PathVariable UUID markId){
        return ResponseEntity.ok(markService.getOneMark(markId));
    }


    @GetMapping("/groups/{groupId}/archive-marks")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public ResponseEntity<ApiResponse<ResPageable>> archiveMarks(
            @AuthenticationPrincipal CustomUserDetails cud,
            @PathVariable UUID sinfId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(markService.getGroupByArchiveMarks(cud, sinfId, keyword, page, size));
    }

}
