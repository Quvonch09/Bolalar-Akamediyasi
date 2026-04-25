package com.example.bolalarakademiyasi.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String phone;
    private String sinfName;
    private String parentFirstName;
    private String parentLastName;
    private String level;
    private UUID sinfId;
    private String imgUrl;
    private String role;
}
