package com.example.bolalarakademiyasi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class LessonDTO {

    @Schema(hidden = true)
    private UUID id;

    private String name;

    private String description;

    private String fileUrl;

    private UUID subjectId;

    private String subjectName;


}
