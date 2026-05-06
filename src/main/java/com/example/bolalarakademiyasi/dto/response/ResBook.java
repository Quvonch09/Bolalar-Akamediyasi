package com.example.bolalarakademiyasi.dto.response;

import com.example.bolalarakademiyasi.dto.BookPageResponse;
import com.example.bolalarakademiyasi.entity.BookPage;
import com.example.bolalarakademiyasi.entity.Lesson;
import com.example.bolalarakademiyasi.entity.Subject;
import com.example.bolalarakademiyasi.entity.enums.BookStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResBook {

    private UUID id;

    private String title;

    private String description;

    // Main file URL stored in Cloudflare
    private String fileUrl;

    private String coverImageUrl;

    private String originalFileName;

    private String status; // PROCESSING, READY, FAILED

    private Integer totalPages;

    //  SCHOOL INTEGRATION
    private Integer gradeLevel;

    private String subjectName;
    private UUID subjectId;

    private String lessonName;
    private UUID lessonId;

    //  RELATION
    private List<BookPageResponse> pages = new ArrayList<>();
}
