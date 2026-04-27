package com.example.bolalarakademiyasi.repository;

import com.example.bolalarakademiyasi.entity.Class;
import com.example.bolalarakademiyasi.entity.Feedback;
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
public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {

    Optional<Feedback> findByIdAndActiveTrue(UUID id);
//
//    @Query(value = "select COUNT(*) from feedback f where f.teacher_id = :teacherId and f.active = true",
//            nativeQuery = true)
//    long countByTeacherId(@Param("teacherId") UUID teacherId);


    @Query(value = """
            select f.* from feedback f
            join users u on f.teacher_id = u.id
            where f.active = true
            and (:title is null or LOWER(f.title) like LOWER(CONCAT('%', :title, '%')))
            and (:teacherName is null
                or LOWER(u.first_name) like LOWER(CONCAT('%', :teacherName, '%'))
                or LOWER(u.last_name)  like LOWER(CONCAT('%', :teacherName, '%')))
            order by  f.created_at desc 
            """, nativeQuery = true)
    Page<Feedback> searchFeedback(@Param("title") String title,
                                  @Param("teacherName") String teacherName,
                                  Pageable pageable);




}