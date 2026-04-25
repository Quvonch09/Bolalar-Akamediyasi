package com.example.bolalarakademiyasi.entity;

import com.example.bolalarakademiyasi.entity.base.BaseEntity;
import com.example.bolalarakademiyasi.entity.enums.MarkCategoryStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
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

    private Integer homeworkScore;

    private Integer activeScore;

    private Integer behaviourScore;

    private Integer totalScore;

    @Enumerated(EnumType.STRING)
    private MarkCategoryStatus markCategoryStatus;

}
