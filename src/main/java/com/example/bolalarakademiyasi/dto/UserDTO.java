package com.example.bolalarakademiyasi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String phone;
    private String imageUrl;
    @Schema(hidden = true)
    private String role;
}
