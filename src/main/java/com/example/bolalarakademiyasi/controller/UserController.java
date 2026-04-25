package com.example.bolalarakademiyasi.controller;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.request.ReqPassword;
import com.example.bolalarakademiyasi.dto.response.ResPageable;
import com.example.bolalarakademiyasi.entity.enums.Role;
import com.example.bolalarakademiyasi.security.CustomUserDetails;
import com.example.bolalarakademiyasi.service.AuthService;
import com.example.bolalarakademiyasi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;
    private final UserService userService;

    @GetMapping
    @Operation(description = "ROLE_TEACHER , ROLE_ADMIN , ROLE_PARENT")
    public ResponseEntity<ApiResponse<ResPageable>> getAllUsersPage(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size,
                                                                    @RequestParam(required = false) Role role,
                                                                    @RequestParam(required = false) String name,
        @Pattern(
             regexp = "^998(9[012345789]|6[0123456789]|7[0123456789]|8[0123456789]|3[0123456789]|5[0123456789])[0-9]{7}$",
             message = "Telefon raqam xato kiritilgan"
        )
                                                                   @Valid @RequestParam(required = false) String phone){
        return ResponseEntity.ok(userService.getAllUsersSearch(name,phone,role,page,size));
    }

    @GetMapping("/me")
    public ApiResponse<?> getProfile(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return userService.getProfile(currentUser);
    }


    @PutMapping("/update-password")
    @Operation(summary = "Parolni almashtirish Hamma role uchun")
    public ResponseEntity<ApiResponse<String>> updatePassword(@Valid  @RequestBody ReqPassword reqPassword) {
        return ResponseEntity.ok(authService.updatePassword(reqPassword));
    }


//    @PutMapping("/activateStudent/{studentId}")
//    public ResponseEntity<ApiResponse<String>> activateStudent(@PathVariable Long studentId) {
//        return ResponseEntity.ok(userService.approveStudent(studentId));
//    }

}
