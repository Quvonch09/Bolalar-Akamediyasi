package com.example.bolalarakademiyasi.entity;

import com.example.bolalarakademiyasi.entity.base.BaseEntity;
import com.example.bolalarakademiyasi.entity.enums.BookStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    //  STORAGE (cloud)
    @Column(nullable = false)
    private String pdfUrl;

    private String coverImageUrl;

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
