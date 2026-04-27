package com.example.bolalarakademiyasi.repository;


import com.example.bolalarakademiyasi.entity.Feedback;
import com.example.bolalarakademiyasi.entity.Homework;
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
public interface HomeworkRepository extends JpaRepository<Homework, UUID> {

//    List<Homework> findAllByStudentIdAndActiveTrue(UUID teacherId);


    Optional<Homework> findByIdAndActiveTrue(UUID id);


//    @Query(value = """
//        SELECT h.* FROM homework h
//                   JOIN lesson l ON h.lesson_id = l.id
//                   WHERE h.active = true
//                   AND (:title IS NULL OR LOWER(h.title) LIKE LOWER(CONCAT('%', :title, '%')))
//                   AND (:deadlineEnum IS NULL OR h.deadline_enum = :deadlineEnum)
//                   AND (:lessonTitle IS NULL OR LOWER(l.title) LIKE LOWER(CONCAT('%', :lessonTitle, '%')))
//                   ORDER BY h.created_at DESC
//""", nativeQuery = true)
//    Page<Homework> searchHomework(@Param("title") String title,
//                                  @Param("deadlineEnum") String deadlineEnum,
//                                  @Param("lessonTitle") String lessonTitle,
//                                  Pageable pageable);




    @Query(value = """
            SELECT h.* FROM homework h
            JOIN lesson l ON h.lesson_id = l.id
            WHERE h.active = true
            AND (:title IS NULL OR LOWER(h.title) LIKE LOWER(CONCAT('%', :title, '%')))
            AND (:deadlineEnum IS NULL OR h.deadline_enum = :deadlineEnum)
            AND (:lessonTitle IS NULL OR LOWER(l.title) LIKE LOWER(CONCAT('%', :lessonTitle, '%')))
            ORDER BY h.created_at DESC
            """, nativeQuery = true)
    Page<Homework> searchHomework(@Param("title") String title,
                                  @Param("deadlineEnum") String deadlineEnum,
                                  @Param("lessonTitle") String lessonTitle,
                                  Pageable pageable);

}
