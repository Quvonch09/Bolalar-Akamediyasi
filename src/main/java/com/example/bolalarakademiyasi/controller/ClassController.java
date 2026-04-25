package com.example.bolalarakademiyasi.controller;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.request.ReqClass;
import com.example.bolalarakademiyasi.dto.request.ReqClassDTO;
import com.example.bolalarakademiyasi.dto.response.ResClass;
import com.example.bolalarakademiyasi.dto.response.ResPageable;
import com.example.bolalarakademiyasi.service.ClassService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor


public class ClassController {


    private final ClassService classService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    @Operation(description = "Shift type = SMENA_1, SMENA_2 ")
    public ResponseEntity<ApiResponse<String>> saveClass(@RequestBody ReqClass reqClass) {
        return ResponseEntity.ok(classService.saveClass(reqClass));
    }

    @DeleteMapping("{/classId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteClass(@PathVariable UUID classId) {
        return ResponseEntity.ok(classService.deleteClass(classId));
    }

    @GetMapping("/{classId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ResClass>> getOneClass(@PathVariable UUID classId) {
        return ResponseEntity.ok(classService.getOneClass(classId));
    }



    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<String>> updateClass(@RequestBody ReqClassDTO reqClassDTO) {
        return ResponseEntity.ok(classService.updateClass(reqClassDTO));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ResClass>>> getAllClasses() {
        return ResponseEntity.ok(classService.getAllClasses());
    }

//    @GetMapping
//
//    public ResponseEntity<ApiResponse<ResPageable>> getAllClasses(@RequestParam(required = false) String name,
//                                                                  @RequestParam(defaultValue = "0") int page,
//                                                                  @RequestParam(defaultValue = "10") int size) {
//        return ResponseEntity.ok(classService.getAllClassesSearch(name, page, size));
//
//
//    }




}
