package com.example.bolalarakademiyasi.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ResLesson {
    private UUID id;

    private String name;

    private String description;

    private String fileUrl;

    private UUID subjectId;

    private String subjectName;



}
