package com.example.bolalarakademiyasi.entity;

import com.example.bolalarakademiyasi.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Lesson extends BaseEntity {
    private String name;
    private String description;
    private String fileUrl; //dars ishlanmasi

    @ManyToOne
    private Subject subject;
}
