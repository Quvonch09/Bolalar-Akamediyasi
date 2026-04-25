package com.example.bolalarakademiyasi.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResStudent {

    private UUID id;
    private String firstName;
    private String lastName;
    private int age;
    private String imgUrl;
    private String phoneNumber;
    private UUID sinfId;
    private String sinfName;
    private UUID parentId;
    private String parentFirstName;
    private String parentLastName;
    private String parentPhone;

}
