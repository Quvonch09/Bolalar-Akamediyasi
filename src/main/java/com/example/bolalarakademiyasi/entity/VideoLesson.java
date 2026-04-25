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
public class VideoLesson extends BaseEntity {
    private String title;

    private String videoUrl;      // cloud link
    private Integer duration;     // sekund

    private Long fileSize;        // optional
    private String format;        // mp4, m3u8

    @ManyToOne
    private Lesson lesson;
}
