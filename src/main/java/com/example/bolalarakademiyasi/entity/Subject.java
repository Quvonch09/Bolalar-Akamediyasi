package com.example.bolalarakademiyasi.entity;

import com.example.bolalarakademiyasi.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Subject extends BaseEntity {
    private String name;
    private String description;
}
