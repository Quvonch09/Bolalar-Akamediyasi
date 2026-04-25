package com.example.bolalarakademiyasi.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ReqStudent {
    private String firstName;
    private String lastName;

    @Pattern(regexp = "^998(9[012345789]|6[0123456789]|7[0123456789]|8[0123456789]|3[0123456789]|5[0123456789])[0-9]{7}$",
    message = "Telefon raqam xato kiritilgan")
    private String phone;
    private String imgUrl;
    private String password;
    private UUID groupId;

    @Pattern(regexp = "^998(9[012345789]|6[0123456789]|7[0123456789]|8[0123456789]|3[0123456789]|5[0123456789])[0-9]{7}$",
    message = "Telefon raqam xato kiritilgan")
    private String parentPhone;
    private String parentName;
}
