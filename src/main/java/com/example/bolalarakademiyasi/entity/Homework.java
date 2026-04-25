package com.example.bolalarakademiyasi.entity;

import com.example.bolalarakademiyasi.entity.base.BaseEntity;
import com.example.bolalarakademiyasi.entity.enums.DeadlineEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Homework extends BaseEntity {

    private String title;

    private String description;

    private Integer deadline; // muddat

    @Enumerated(EnumType.STRING)
    private DeadlineEnum  deadlineEnum;  // deadline turi -> soat, kun, hafta, oy

    @ManyToOne
    private Lesson lesson;
}
