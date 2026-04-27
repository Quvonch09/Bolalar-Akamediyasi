package com.example.bolalarakademiyasi.dto.response;

import com.example.bolalarakademiyasi.entity.BookPage;
import com.example.bolalarakademiyasi.entity.Lesson;
import com.example.bolalarakademiyasi.entity.Subject;
import com.example.bolalarakademiyasi.entity.enums.BookStatus;
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
public class ResBook {

    private UUID id;

    private String title;

    private String description;

    //  STORAGE (cloud)
    private String pdfUrl;

    private String coverImageUrl;

    private String originalFileName;

    private String pdfPath;

    private String status; // PROCESSING, READY, FAILED

    private Integer totalPages;

    //  SCHOOL INTEGRATION
    private Integer gradeLevel;

    private String subjectName;
    private UUID subjectId;

    private String lessonName;
    private UUID lessonId;

    //  RELATION
    private List<BookPage> pages = new ArrayList<>();
}
