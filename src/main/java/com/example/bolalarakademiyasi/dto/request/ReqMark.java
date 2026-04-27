package com.example.bolalarakademiyasi.dto.request;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqMark {
    private UUID studentId;
    private int homeworkScore;
    private int activityScore;
    private int behaviourScore;
    private int totalScore;
    private LocalDate date;
}
