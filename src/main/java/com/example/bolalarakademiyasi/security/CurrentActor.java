package com.example.bolalarakademiyasi.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CurrentActor {

    private UUID id;
    private String role;
    private String firstName;
    private String lastName;
    private boolean anonymous;

    public static CurrentActor user(UUID id, String role, String firstName, String lastName) {
        return new CurrentActor(id, role, firstName, lastName, false);
    }

    public static CurrentActor student(UUID id, String role, String firstName, String lastName) {
        return new CurrentActor(id, role, firstName,lastName, false);
    }

    public static CurrentActor anonymous() {
        return new CurrentActor(null, "ANONYMOUS", "Anonymous", "Anonymous",true);
    }
}
