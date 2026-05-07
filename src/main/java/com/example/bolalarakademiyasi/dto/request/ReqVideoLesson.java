package com.example.bolalarakademiyasi.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqVideoLesson {
    @NotNull(message = "Lesson ID bo'sh bo'lishi mumkin emas")
    private UUID lessonId;

    private String title;
    private String videoUrl;
    private Integer duration;
    private Long fileSize;
    private String format;
}
