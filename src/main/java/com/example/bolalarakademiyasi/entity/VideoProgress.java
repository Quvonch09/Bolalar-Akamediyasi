package com.example.bolalarakademiyasi.entity;

import com.example.bolalarakademiyasi.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class VideoProgress extends BaseEntity {
    private Integer watchedSeconds;
    private Boolean completed;

    private LocalDateTime lastWatchedAt;

    @ManyToOne
    private Student student;

    @ManyToOne
    private VideoLesson videoLesson;
}
