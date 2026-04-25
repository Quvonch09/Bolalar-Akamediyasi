package com.example.bolalarakademiyasi.security;

import com.example.bolalarakademiyasi.entity.Student;
import com.example.bolalarakademiyasi.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {

    public CurrentActor getCurrentActor() {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return CurrentActor.anonymous();
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof CustomUserDetails cud) {

            if (cud.isUser()) {
                User u = cud.getUser();
                return CurrentActor.user(
                        u.getId(),
                        u.getRole().name(),
                        u.getFirstName(),
                        u.getLastName()
                );
            }

            if (cud.isStudent()) {
                Student s = cud.getStudent();
                return CurrentActor.student(
                        s.getId(),
                        "ROLE_STUDENT",
                        s.getFirstName(),
                        s.getLastName()
                );
            }
        }

        return CurrentActor.anonymous();
    }
}

