package com.example.bolalarakademiyasi.controller;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.request.ReqLesson;
import com.example.bolalarakademiyasi.dto.request.ReqSubject;
import com.example.bolalarakademiyasi.dto.response.ResLesson;
import com.example.bolalarakademiyasi.dto.response.ResPageable;
import com.example.bolalarakademiyasi.dto.response.ResSubject;
import com.example.bolalarakademiyasi.entity.Subject;
import com.example.bolalarakademiyasi.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/subject")
@RequiredArgsConstructor
public class SubjectController {


    private final SubjectService subjectService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_TEACHER')")
    public ResponseEntity<ApiResponse<String>> save(@RequestBody ReqSubject reqSubject) {
        return ResponseEntity.ok(subjectService.saveSubject(reqSubject));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_TEACHER')")
    public ResponseEntity<ApiResponse<String>> delete (@PathVariable UUID id) {
        return ResponseEntity.ok(subjectService.deleteSubject(id));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResSubject>> getOneSubject(@PathVariable UUID id) {
        return ResponseEntity.ok(subjectService.getOneSubject(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_TEACHER')")
    public ResponseEntity<ApiResponse<String>>  update (@PathVariable UUID id, @RequestBody ReqSubject reqSubject) {
        return ResponseEntity.ok(subjectService.updateSubject(id, reqSubject));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<ResPageable>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(subjectService.searchSubject(name, description, page, size));
    }

}
