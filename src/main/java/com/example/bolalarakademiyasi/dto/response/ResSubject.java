package com.example.bolalarakademiyasi.dto.response;

import lombok.*;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResSubject {

    private UUID id;

    private String name;

    private String description;
}
