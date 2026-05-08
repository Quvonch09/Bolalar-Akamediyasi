package com.example.bolalarakademiyasi.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqProduct {

    @NotBlank(message = "Product name cannot be empty")
    private String name;

    @Size(max = 50, message = "Description must not exceed 50 characters")
    private String description;

    @NotNull(message = "Count coin is required")
    @Min(value = 1, message = "Coin count must be 0 or greater")
    private Integer countCoin;

    private String imgUrl;
}