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
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByPhoneAndEnabledTrue(String phone);
    boolean existsByPhone(String phone);
    boolean existsByPhoneAndActiveTrue(String phone);

    Optional<User> findByIdAndEnabledTrue(UUID id);
    Optional<User> findByPhoneAndRole(String phone, Role role);

    @Query(value = """
    select u from users u
    where (:name is null or :name = '' or lower(u.firstName) like lower(concat('%', :name, '%'))
           or lower(u.lastName) like lower(concat('%', :name, '%')))
      and (:phone is null or :phone = '' or u.phone like concat('%', :phone, '%'))
      and (:role is null or u.role = :role) and u.role <> 'ROLE_SUPER_ADMIN' and u.active = true
""", nativeQuery = true)
    Page<User> searchUser(@Param("name") String name,
                          @Param("phone") String phone,
                          @Param("role") Role role,
                          Pageable pageable);

    List<User> findAllByRole(Role role);
}
