package com.example.bolalarakademiyasi.dto.request;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqMarkDTO {
    private UUID id;
    private UUID studentId;
    private int homeworkScore;
    private int activityScore;
    private int totalScore;
}
