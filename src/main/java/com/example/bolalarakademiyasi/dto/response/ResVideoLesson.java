package com.example.bolalarakademiyasi.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResVideoLesson {
    private UUID id;
    private String title;
    private String videoUrl;
    private Integer duration;
    private Long fileSize;
    private String format;
    private UUID lessonId;
    private String lessonName;
    private UUID subjectId;
    private String subjectName;
}
