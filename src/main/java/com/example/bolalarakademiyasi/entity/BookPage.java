package com.example.bolalarakademiyasi.entity;

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
@Entity
public class BookPage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    private Integer pageNumber;

    // 🔥 cloud image
    private String imageUrl;

    private Integer width;
    private Integer height;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PageBlock> blocks = new ArrayList<>();
}
