package com.example.bolalarakademiyasi.entity;

import com.example.bolalarakademiyasi.entity.base.BaseEntity;
import com.example.bolalarakademiyasi.entity.enums.PageBlockType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class PageBlock extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private BookPage page;

    @Enumerated(EnumType.STRING)
    private PageBlockType type;

    private String title;

    @Column(columnDefinition = "text")
    private String body;

    private String imageUrl;

    // 📍 position
    private Double x;
    private Double y;
    private Double width;
    private Double height;

    // 🔥 INTERACTIVE
    private Boolean required;      // majburiy block
    private Boolean completed;     // student bajarganmi

}