package com.example.bolalarakademiyasi.entity;

import com.example.bolalarakademiyasi.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.Instant;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class BookProgress extends BaseEntity {

    @ManyToOne
    private Student student;

    @ManyToOne
    private Book book;

    private Integer currentPage;
    private Integer totalPages;

    private Double progressPercent;

    private Boolean completed;

    private Instant lastReadAt;
}
