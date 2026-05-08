package com.example.bolalarakademiyasi.service;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.request.ReqProduct;
import com.example.bolalarakademiyasi.dto.response.ResPageable;
import com.example.bolalarakademiyasi.dto.response.ResProduct;
import com.example.bolalarakademiyasi.entity.Product;
import com.example.bolalarakademiyasi.exception.DataNotFoundException;
import com.example.bolalarakademiyasi.mapper.ProductMapper;
import com.example.bolalarakademiyasi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ApiResponse<String> saveProduct(ReqProduct req) {
        Product product = productMapper.toEntity(req);
        productRepository.save(product);
        return ApiResponse.success(null, "Product saved successfully");
    }

    public ApiResponse<String> updateProduct(UUID id, ReqProduct req) {
        Product product = productRepository.findByIdAndActiveTrue(id).orElseThrow(
                () -> new DataNotFoundException("Product not found")
        );

        productMapper.updateProductFromDto(req, product);
        productRepository.save(product);

        return ApiResponse.success(null, "Product updated successfully");
    }

    public ApiResponse<String> deleteProduct(UUID id) {
        Product product = productRepository.findByIdAndActiveTrue(id).orElseThrow(
                () -> new DataNotFoundException("Product not found")
        );

        product.softDelete();
        productRepository.save(product);

        return ApiResponse.success(null, "Product deleted successfully");
    }

    public ApiResponse<ResProduct> getOneProduct(UUID id) {
        Product product = productRepository.findByIdAndActiveTrue(id).orElseThrow(
                () -> new DataNotFoundException("Product not found")
        );
        
        return ApiResponse.success(productMapper.toResProduct(product), "Success");
    }

    public ApiResponse<ResPageable> getAllProducts(String name, int page, int size) {

        String searchTerm = (name == null) ? "" : name;

        Page<Product> products = productRepository.searchProducts(
                searchTerm, PageRequest.of(page, size)
        );

        if (products.isEmpty()) {
            throw new DataNotFoundException("Products not found");
        }

        List<ResProduct> list = products.getContent().stream()
                .map(productMapper::toResProduct)
                .collect(Collectors.toList());

        ResPageable resPageable = ResPageable.builder()
                .page(page)
                .size(size)
                .totalElements(products.getTotalElements())
                .totalPage(products.getTotalPages())
                .body(list)
                .build();

        return ApiResponse.success(resPageable, "Success");
    }
}