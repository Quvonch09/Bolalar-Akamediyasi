package com.example.bolalarakademiyasi.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StudentDTO {
    private UUID id;

    private String firstName;

    private String lastName;

    @Pattern(
            regexp = "^998(9[012345789]|6[0123456789]|7[0123456789]|8[0123456789]|3[0123456789]|5[0123456789])[0-9]{7}$",
            message = "Telefon raqam xato kiritilgan"
    )
    private String phone;

    @Pattern(
            regexp = "^998(9[012345789]|6[0123456789]|7[0123456789]|8[0123456789]|3[0123456789]|5[0123456789])[0-9]{7}$",
            message = "Telefon raqam xato kiritilgan"
    )
    private String parentPhone;

    private String imgUrl;
}
