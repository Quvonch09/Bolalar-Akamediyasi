package com.example.bolalarakademiyasi.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResFeedback {

    private UUID id;

    private String title;

    private int rating;

    private UUID teacherId;
}