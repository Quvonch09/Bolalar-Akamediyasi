package com.example.bolalarakademiyasi.repository;

import com.example.bolalarakademiyasi.entity.Submission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SubmissionRepository extends JpaRepository<Submission, UUID> {

    Optional<Submission> findByIdAndActiveTrue(UUID id);

    Page<Submission> findAllByHomeworkIdAndActiveTrue(UUID homeworkId, Pageable pageable);

    Page<Submission> findAllByActiveTrue(Pageable pageable);

    @Query(value = "SELECT s FROM Submission s " +
                   "LEFT JOIN s.student st " +
                   "LEFT JOIN s.homework hw " +
                   "WHERE s.active = true " +
                   "AND (:studentName IS NULL OR LOWER(st.firstName) LIKE :studentName OR LOWER(st.lastName) LIKE :studentName) " +
                   "AND (:homeworkTitle IS NULL OR LOWER(hw.title) LIKE :homeworkTitle) " +
                   "AND (:isGraded IS NULL OR (:isGraded = true AND s.score > 0) OR (:isGraded = false AND (s.score = 0 OR s.score IS NULL)))",
           countQuery = "SELECT COUNT(s) FROM Submission s " +
                        "LEFT JOIN s.student st " +
                        "LEFT JOIN s.homework hw " +
                        "WHERE s.active = true " +
                        "AND (:studentName IS NULL OR LOWER(st.firstName) LIKE :studentName OR LOWER(st.lastName) LIKE :studentName) " +
                        "AND (:homeworkTitle IS NULL OR LOWER(hw.title) LIKE :homeworkTitle) " +
                        "AND (:isGraded IS NULL OR (:isGraded = true AND s.score > 0) OR (:isGraded = false AND (s.score = 0 OR s.score IS NULL)))")
    Page<Submission> searchSubmissions(
            @Param("studentName") String studentName,
            @Param("homeworkTitle") String homeworkTitle,
            @Param("isGraded") Boolean isGraded,
            Pageable pageable
    );
}