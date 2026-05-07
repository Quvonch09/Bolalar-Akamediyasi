package com.example.bolalarakademiyasi.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqVideoProgress {
    @NotNull(message = "Student ID bo'sh bo'lishi mumkin emas")
    private UUID studentId;

    @NotNull(message = "VideoLesson ID bo'sh bo'lishi mumkin emas")
    private UUID videoLessonId;

    @NotNull(message = "Watched seconds bo'sh bo'lishi mumkin emas")
    private Integer watchedSeconds;

    private Integer lastPositionSeconds;
    private Boolean completed;
}
