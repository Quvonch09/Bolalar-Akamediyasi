package com.example.bolalarakademiyasi.security;

import com.example.bolalarakademiyasi.entity.Student;
import com.example.bolalarakademiyasi.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private final User user;
    private final Student student;

    private final String firstName;
    private final String lastName;
    private final String phone;
    private final String password;
    private final String role;
    private final String imgUrl;
    private final boolean enabled;

    public CustomUserDetails(User user) {
        this.user = user;
        this.student = null;
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phone = user.getPhone();
        this.password = user.getPassword();
        this.role = user.getRole().name();
        this.imgUrl = user.getImgUrl();
        this.enabled = user.isEnabled();
    }

    public CustomUserDetails(Student student) {
        this.user = null;
        this.student = student;
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.phone = student.getPhone();
        this.password = student.getPassword();
        this.role = "ROLE_STUDENT";
        this.imgUrl = student.getImgUrl();
        this.enabled = student.isActive();
    }

    public boolean isUser() {
        return user != null;
    }

    public boolean isStudent() {
        return student != null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of((GrantedAuthority) () -> role);
    }

    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return phone; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return enabled; }

    public static CustomUserDetails fromUser(User user) {
        return new CustomUserDetails(user);
    }

    public static CustomUserDetails fromStudent(Student student) {
        return new CustomUserDetails(student);
    }
}

