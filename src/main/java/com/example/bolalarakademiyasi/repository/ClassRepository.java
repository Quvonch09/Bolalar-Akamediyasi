package com.example.bolalarakademiyasi.repository;

import com.example.bolalarakademiyasi.entity.Class;
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
public interface   ClassRepository extends JpaRepository<Class, UUID> {
    List<Class> findAllByTeacherIdAndActiveTrue(UUID teacherId);

    Optional<Class> findByIdAndActiveTrue(UUID sinfId);

    @Query(value = "select g.id from class g where g.teacher_id = :teacherId and g.active = true", nativeQuery = true)
    List<UUID> findIdsByTeacherId(UUID teacherId);


    @Query(value = """
        select c.* from class c join users u on c.teacher_id = u.id where
        (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND
        (:teacherName IS NULL OR LOWER(u.first_name) LIKE LOWER(CONCAT('%', :teacherName, '%'))
            or LOWER(u.last_name) LIKE LOWER(CONCAT('%', :teacherName, '%'))) order by created_at desc
        """, nativeQuery = true)
    Page<Class> searchClass(@Param("name") String name, @Param("teacherName") String teacherName,
                          Pageable pageable);

}
