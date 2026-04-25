package com.example.bolalarakademiyasi.controller;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.StudentDTO;
import com.example.bolalarakademiyasi.dto.request.ReqStudent;
import com.example.bolalarakademiyasi.dto.response.ResPageable;
import com.example.bolalarakademiyasi.dto.response.ResStudent;
import com.example.bolalarakademiyasi.security.CustomUserDetails;
import com.example.bolalarakademiyasi.service.AuthService;
import com.example.bolalarakademiyasi.service.MarkService;
import com.example.bolalarakademiyasi.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final MarkService markService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<ApiResponse<ResPageable>> getStudentsByPage(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size,
                                                                      @RequestParam(required = false) String name,
                                                                      @RequestParam(required = false) String phone) {
        return ResponseEntity.ok(studentService.getStudents(name,phone,page,size));
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<ApiResponse<ResStudent>> getById(@PathVariable UUID studentId) {
        return ResponseEntity.ok(studentService.getById(studentId));
    }
    @DeleteMapping("/{studentId}")
    public ResponseEntity<ApiResponse<String>> deleteById(@PathVariable UUID studentId) {
        return ResponseEntity.ok(studentService.delete(studentId));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<String>> update(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                      @RequestBody StudentDTO studentDTO){
        return ResponseEntity.ok(studentService.update(customUserDetails, studentDTO));

    }

    @PutMapping("/update-group")
    public ResponseEntity<ApiResponse<String>> updateGroup(@RequestParam UUID studentId,
                                                           @RequestParam UUID sinfId){
        return ResponseEntity.ok(studentService.updateGroup(studentId, sinfId));
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    @PostMapping("/saveStudent")
    public ResponseEntity<ApiResponse<String>> studentLogin(@RequestBody ReqStudent reqStudent){
        return ResponseEntity.ok(authService.saveStudent(reqStudent));
    }



    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<ResStudent>>> getList(){
        return ResponseEntity.ok(studentService.getStudentList());
    }


//    @GetMapping("/inactive-student")
//    public ResponseEntity<ApiResponse<List<ResStudent>>> getInactiveList(){
//        return ResponseEntity.ok(studentService.getDeActiveStudentList());
//    }

}
