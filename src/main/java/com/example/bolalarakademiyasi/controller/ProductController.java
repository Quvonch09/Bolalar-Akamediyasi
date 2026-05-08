package com.example.bolalarakademiyasi.controller;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.request.ReqProduct;
import com.example.bolalarakademiyasi.dto.response.ResPageable;
import com.example.bolalarakademiyasi.dto.response.ResProduct;
import com.example.bolalarakademiyasi.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    @Operation(summary = "Yangi Product (Sovg'a) qo'shish")
    public ResponseEntity<ApiResponse<String>> saveProduct(@Valid @RequestBody ReqProduct req) {
        return ResponseEntity.ok(productService.saveProduct(req));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    @Operation(summary = "Mavjud Productni tahrirlash")
    public ResponseEntity<ApiResponse<String>> updateProduct(
            @PathVariable UUID id, 
            @Valid @RequestBody ReqProduct req) {
        return ResponseEntity.ok(productService.updateProduct(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    @Operation(summary = "Productni o'chirish (Soft Delete)")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Bitta Product haqida ma'lumot olish")
    public ResponseEntity<ApiResponse<ResProduct>> getOneProduct(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getOneProduct(id));
    }

    @GetMapping
    @Operation(summary = "Barcha Productlarni ro'yxatini olish (Paginatsiya bilan)")
    public ResponseEntity<ApiResponse<ResPageable>> getAllProducts(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productService.getAllProducts(name, page, size));
    }
}