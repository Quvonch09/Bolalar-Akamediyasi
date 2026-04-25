package com.example.bolalarakademiyasi.security;

import com.example.bolalarakademiyasi.entity.Student;
import com.example.bolalarakademiyasi.entity.User;
import com.example.bolalarakademiyasi.repository.StudentRepository;
import com.example.bolalarakademiyasi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        // Avval User ni tekshiramiz
        User user = userRepository.findByPhoneAndEnabledTrue(phone).orElse(null);
        if (user != null) {
            return CustomUserDetails.fromUser(user);
        }

        // Agar User topilmasa → Student ni tekshiramiz
        Student student = studentRepository.findByPhoneAndActiveTrue(phone).orElse(null);
        if (student != null) {
            return CustomUserDetails.fromStudent(student);
        }

        // Ikkalasida ham topilmasa → exception
        throw new UsernameNotFoundException("User yoki Student topilmadi: " + phone);
    }
}
