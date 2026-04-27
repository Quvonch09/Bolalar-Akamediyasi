package com.example.bolalarakademiyasi.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqFeedback {

    @NotBlank(message = "Sarlavha bo'sh bo'lishi mumkin emas")
    private String title;

    @Min(value = 1, message = "Reyting kamida 1 bo'lishi kerak")
    @Max(value = 5, message = "Reyting ko'pi bilan 5 bo'lishi kerak")
    private int rating;

    @NotNull(message = "O'qituvchi ID si bo'sh bo'lishi mumkin emas")
    private UUID teacherId;
}