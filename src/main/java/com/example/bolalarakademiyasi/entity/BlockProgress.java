package com.example.bolalarakademiyasi.entity;

import com.example.bolalarakademiyasi.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.time.Instant;
import java.util.UUID;

@Entity
public class BlockProgress extends BaseEntity {

    private UUID studentId;

    @ManyToOne
    private PageBlock block;

    private Boolean answered;
    private Boolean correct;

    private String answer;

    private Instant answeredAt;
}
