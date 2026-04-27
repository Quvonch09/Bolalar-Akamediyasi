package com.example.bolalarakademiyasi.entity;

import com.example.bolalarakademiyasi.entity.base.BaseEntity;
import com.example.bolalarakademiyasi.entity.enums.BookStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Book extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    //  STORAGE (cloud)
    @Column(nullable = false)
    private String pdfUrl;

    private String coverImagePath;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private String pdfPath;

    @Enumerated(EnumType.STRING)
    private BookStatus status; // PROCESSING, READY, FAILED

    private Integer totalPages;

    //  SCHOOL INTEGRATION
    private Integer gradeLevel;

    @ManyToOne
    private Subject subject;

    @ManyToOne
    private Lesson lesson;

    //  RELATION
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("pageNumber asc")
    private List<BookPage> pages = new ArrayList<>();
}
