package com.example.bolalarakademiyasi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class SubmissionDTO {


    private UUID id;

    private String textAnswer;

    private String fileUrl;

    @Max(10)
    @Min(1)
    private int score; // 10 ball

    private String feedback;

    private UUID studentId;

    private String studentName;

    private UUID homeworkId;

    private String homeworkName;
}
