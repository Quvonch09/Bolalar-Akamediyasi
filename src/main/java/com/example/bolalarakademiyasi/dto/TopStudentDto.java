package com.example.bolalarakademiyasi.dto;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TopStudentDto {
    private UUID studentId;
    private String studentFirstName;
    private String studentLastName;
    private Integer totalScore;
    private String imageUrl;
}