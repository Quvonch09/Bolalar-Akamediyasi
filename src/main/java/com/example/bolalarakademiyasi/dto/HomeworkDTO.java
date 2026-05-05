package com.example.bolalarakademiyasi.dto;


import com.example.bolalarakademiyasi.entity.Lesson;
import com.example.bolalarakademiyasi.entity.enums.DeadlineEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HomeworkDTO {


    @Schema(hidden = true)
    private UUID id;

    private String title;

    private String description;

    private Integer deadline; // muddat

    @Enumerated(EnumType.STRING)
    private DeadlineEnum deadlineEnum;  // deadline turi -> soat, kun, hafta, oy

    private UUID lessonId;

    private String lessonName;
}
