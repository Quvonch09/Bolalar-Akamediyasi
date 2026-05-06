package com.example.bolalarakademiyasi.entity;

import com.example.bolalarakademiyasi.entity.base.BaseEntity;
import jakarta.persistence.Access;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Submission extends BaseEntity {

    private String textAnswer;

    private String fileUrl;

    @Max(10)
    @Min(0)
    private Integer score; // 10 ball

    private String feedback;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Homework homework;


}
