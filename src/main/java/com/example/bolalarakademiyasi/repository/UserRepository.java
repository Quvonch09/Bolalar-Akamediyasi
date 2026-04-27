package com.example.bolalarakademiyasi.repository;

import com.example.bolalarakademiyasi.entity.User;
import com.example.bolalarakademiyasi.entity.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface  UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByPhoneAndEnabledTrue(String phone);
    boolean existsByPhone(String phone);
    boolean existsByPhoneAndActiveTrue(String phone);

    Optional<User> findByIdAndEnabledTrue(UUID id);
    Optional<User> findByPhoneAndRole(String phone, Role role);

    @Query(value = """
    SELECT *
    FROM users u
    WHERE (:name IS NULL OR :name = ''
           OR LOWER(u.first_name) LIKE LOWER(CONCAT('%', :name, '%'))
           OR LOWER(u.last_name) LIKE LOWER(CONCAT('%', :name, '%')))
      AND (:phone IS NULL OR :phone = '' OR u.phone LIKE CONCAT('%', :phone, '%'))
      AND (:role IS NULL OR u.role = :role)
      AND u.role <> 'ROLE_SUPER_ADMIN'
      AND u.enabled = true
""", nativeQuery = true)
    Page<User> searchUser(@Param("name") String name,
                          @Param("phone") String phone,
                          @Param("role") String role,
                          Pageable pageable);

    List<User> findAllByRole(Role role);
}
