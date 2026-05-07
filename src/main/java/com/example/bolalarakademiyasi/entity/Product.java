package com.example.bolalarakademiyasi.entity;

import com.example.bolalarakademiyasi.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Product extends BaseEntity { // coin almashadigan productlar

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    private int countCoin;

    private String imgUrl;
}
