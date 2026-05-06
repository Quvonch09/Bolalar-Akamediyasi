package com.example.bolalarakademiyasi.dto.request;
import com.example.bolalarakademiyasi.entity.enums.DeadlineEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqHomework {

    @NotBlank(message = "Sarlavha bo'sh bo'lishi mumkin emas")
    private String title;

    private String description;

    private Integer deadline; // muddat

    @Enumerated(EnumType.STRING)
    private DeadlineEnum deadlineEnum;  // deadline turi -> soat, kun, hafta, oy

    @NotBlank(message = "LessonMapper ID si bush bulishi mumkin emas")
    private UUID lessonId;


}
