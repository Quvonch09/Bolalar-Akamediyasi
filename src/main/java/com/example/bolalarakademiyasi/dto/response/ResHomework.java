package com.example.bolalarakademiyasi.dto.response;
import com.example.bolalarakademiyasi.entity.Lesson;
import com.example.bolalarakademiyasi.entity.enums.DeadlineEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResHomework {

    private UUID id;

    private String title;

    private String description;

    private Integer deadline; // muddat
//
    @Enumerated(EnumType.STRING)
    private DeadlineEnum deadlineEnum;  // deadline turi -> soat, kun, hafta, oy

    private UUID lessonID;
}
