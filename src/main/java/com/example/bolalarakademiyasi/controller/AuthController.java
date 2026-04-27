package com.example.bolalarakademiyasi.controller;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.AuthDTO;
import com.example.bolalarakademiyasi.dto.AuthLogin;
import com.example.bolalarakademiyasi.dto.request.ReqStudent;
import com.example.bolalarakademiyasi.dto.request.Token;
import com.example.bolalarakademiyasi.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthDTO>> adminLogin(@RequestBody AuthLogin authLogin){
        return ResponseEntity.ok(authService.login(authLogin));
    }


    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<String>> checkToken(@RequestBody Token token){
        return ResponseEntity.ok(authService.validate(token));
    }


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody ReqStudent reqStudent){
        return ResponseEntity.ok(authService.registerUser(reqStudent));
    }

}
