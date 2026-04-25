package com.example.bolalarakademiyasi.repository;

import com.example.bolalarakademiyasi.entity.Student;
import com.example.bolalarakademiyasi.entity.User;
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
public interface StudentRepository extends JpaRepository<Student, UUID> {
    Optional<Student> findByPhoneAndActiveTrue(String phone);
    boolean existsByPhone(String phoneNumber);
    List<Student> findAllBySinf_idAndActiveTrue(UUID sinfId);

    Long countBySinf_id(UUID sinfId);

    List<Student> findAllByParent_IdAndActiveTrue(UUID parent_id);

    @Query(value = """
    select s.* from student s join class g on s.sinf_id = g.id join users u on g.teacher_id = u.id 
                   where u.id = ?1 and s.active = true
    """, nativeQuery = true)
    List<Student> findAllByTeacher(UUID teacherId);

    @Query("""
    select s from Student s
    where (:name is null or :name = '' or lower(s.firstName) like lower(concat('%', :name, '%'))
     or lower(s.lastName) like lower(concat('%', :name, '%')))
      and (:phone is null or :phone = '' or s.phone like concat('%', :phone, '%')) and s.active = true
""")
    Page<Student> searchStudents(@Param("name") String name,
                                 @Param("phone") String phone,
                                 Pageable pageable);

    List<Student> findAllByActiveTrue();




}
