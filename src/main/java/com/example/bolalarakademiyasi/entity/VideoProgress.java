package com.example.bolalarakademiyasi.entity;

import com.example.bolalarakademiyasi.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "video_lesson_id"})
})
public class VideoProgress extends BaseEntity {
    private Integer watchedSeconds;
    private Integer lastPositionSeconds;
    private Boolean completed;

    private LocalDateTime lastWatchedAt;

    @ManyToOne
    private Student student;

    @ManyToOne
    private VideoLesson videoLesson;
}
