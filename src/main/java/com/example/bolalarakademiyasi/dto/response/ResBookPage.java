package com.example.bolalarakademiyasi.dto.response;

import com.example.bolalarakademiyasi.entity.Book;
import com.example.bolalarakademiyasi.entity.PageBlock;
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
public class ResBookPage {

    private UUID id;

    private String bookTitle;
    private UUID bookId;

    private Integer pageNumber;

    // 🔥 cloud image
    private String imageUrl;

    private Integer width;
    private Integer height;

    private List<PageBlock> blocks = new ArrayList<>();
}
