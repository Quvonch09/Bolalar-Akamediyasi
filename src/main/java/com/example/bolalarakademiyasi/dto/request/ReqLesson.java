package com.example.bolalarakademiyasi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ReqLesson {
    @NotBlank(message = "Lesson nomi bush bulishi mumkin emas")
    private String name;

    private String description;

    private String fileUrl;

    @NotNull(message = "Subject ID si bush bulishi mumkin emas")
    private UUID subjectId;

    private ReqVideoLessonPayload videoLesson;

}
