package com.example.bolalarakademiyasi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
