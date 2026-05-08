package com.example.bolalarakademiyasi.mapper;

import com.example.bolalarakademiyasi.dto.request.ReqProduct;
import com.example.bolalarakademiyasi.dto.response.ResProduct;
import com.example.bolalarakademiyasi.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ResProduct toResProduct(Product product) {
        if (product == null) {
            return null;
        }

        return ResProduct.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .countCoin(product.getCountCoin())
                .imgUrl(product.getImgUrl())
                .build();
    }

    public Product toEntity(ReqProduct req) {
        if (req == null) {
            return null;
        }

        Product product = new Product();
        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setCountCoin(req.getCountCoin());
        product.setImgUrl(req.getImgUrl());
        
        return product;
    }

    public void updateProductFromDto(ReqProduct req, Product product) {
        if (req == null || product == null) {
            return;
        }

        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setCountCoin(req.getCountCoin());
        product.setImgUrl(req.getImgUrl());
    }
}