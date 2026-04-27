package com.example.bolalarakademiyasi.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqBook {
    private String title;
    private String description;
    private Integer gradeLevel;
    private MultipartFile file;
    private UUID subjectId;
    private UUID lessonId;
}
