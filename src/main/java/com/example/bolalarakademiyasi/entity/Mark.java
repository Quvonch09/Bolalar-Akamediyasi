package com.example.bolalarakademiyasi.entity;

import com.example.bolalarakademiyasi.entity.base.BaseEntity;
import com.example.bolalarakademiyasi.entity.enums.MarkCategoryStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.Where;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Where(clause = "active = true")
public class Mark extends BaseEntity {

    private LocalDate date;

    @ManyToOne
    private Student student;

    @Max(10)
    @Min(1)
    private Integer homeworkScore;

    @Max(10)
    @Min(1)
    private Integer activeScore;

    @Max(10)
    @Min(1)
    private Integer behaviourScore;

    private Integer totalScore;

    @Enumerated(EnumType.STRING)
    private MarkCategoryStatus markCategoryStatus;

}
