package com.example.bolalarakademiyasi.dto.response;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResProduct {

    private UUID id;
    private String name;
    private String description;
    private int countCoin;
    private String imgUrl;
}