package com.example.bolalarakademiyasi.dto.request;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Subject ID si bush bulishi mumkin emas")
    private UUID subjectId;


}
