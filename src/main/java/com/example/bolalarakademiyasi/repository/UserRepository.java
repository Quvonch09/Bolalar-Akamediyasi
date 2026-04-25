package com.example.bolalarakademiyasi.repository;

import com.example.bolalarakademiyasi.entity.User;
import com.example.bolalarakademiyasi.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByPhoneAndEnabledTrue(String phone);
    boolean existsByPhone(String phone);
    boolean existsByPhoneAndActiveTrue(String phone);


    Optional<User> findByPhoneAndRole(String phone, Role role);

}
