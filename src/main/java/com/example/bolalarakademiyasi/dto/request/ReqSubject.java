package com.example.bolalarakademiyasi.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqSubject {
    @NotBlank(message = "Lesson nomi bush bulishi mumkin emas")
    private String name;

    private String description;



}
