package com.example.bolalarakademiyasi.dto.response;

import com.example.bolalarakademiyasi.entity.User;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ResFeedbackDTO {

    private UUID id;

    private String title;

    @Max(5)
    private int rating;

    private UUID teacherId;
}
