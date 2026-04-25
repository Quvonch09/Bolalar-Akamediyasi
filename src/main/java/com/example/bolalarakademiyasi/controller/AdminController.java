package com.example.bolalarakademiyasi.controller;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.UserDTO;
import com.example.bolalarakademiyasi.dto.request.AuthRegister;
import com.example.bolalarakademiyasi.dto.response.ResPageable;
import com.example.bolalarakademiyasi.dto.response.UserResponse;
import com.example.bolalarakademiyasi.entity.enums.Role;
import com.example.bolalarakademiyasi.security.CustomUserDetails;
import com.example.bolalarakademiyasi.service.AuthService;
import com.example.bolalarakademiyasi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AuthService authService;
    private final UserService userService;


    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/saveUser")
    public ResponseEntity<ApiResponse<String>> userLogin(
            @Valid @RequestBody AuthRegister register
    ){
        return ResponseEntity.ok(authService.saveUser(register, Role.ROLE_ADMIN));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getOneUser(userId));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<String>> updateUser(@AuthenticationPrincipal CustomUserDetails current,
                                                          @RequestBody UserDTO req){
        return ResponseEntity.ok(userService.update(current, req));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID userId){
        return ResponseEntity.ok(userService.deleteById(userId));
    }


    @GetMapping("/list")
    @Operation(summary = "Adminlarning LIST da malumoti")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllList(){
        return ResponseEntity.ok(userService.getAllList(Role.ROLE_ADMIN));
    }


    @GetMapping
    @Operation(summary = "Adminlarni FILTR qilish PAGE da")
    public ResponseEntity<ApiResponse<ResPageable>> getAll(@RequestParam(required = false) String name,
                                              @RequestParam(required = false) String phone,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(userService.getAllUsersSearch(name, phone, Role.ROLE_ADMIN,page,size));
    }
}
