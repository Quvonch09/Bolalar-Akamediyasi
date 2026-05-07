package com.example.bolalarakademiyasi.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResVideoProgress {
    private UUID id;
    private Integer watchedSeconds;
    private Integer lastPositionSeconds;
    private Boolean completed;
    private Double progressPercent;
    private LocalDateTime lastWatchedAt;
    private UUID studentId;
    private String studentName;
    private UUID videoLessonId;
    private String videoLessonTitle;
    private UUID lessonId;
    private String lessonName;
}
