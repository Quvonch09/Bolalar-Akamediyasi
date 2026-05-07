package com.example.bolalarakademiyasi.repository;

import com.example.bolalarakademiyasi.entity.VideoLesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface VideoLessonRepository extends JpaRepository<VideoLesson, UUID> {

    Optional<VideoLesson> findByIdAndActiveTrue(UUID id);

    Optional<VideoLesson> findByLessonIdAndActiveTrue(UUID lessonId);

    boolean existsByLessonIdAndActiveTrue(UUID lessonId);

    @Query(value = """
            select vl from VideoLesson vl
            left join vl.lesson l
            left join l.subject s
            where vl.active = true
            and (:title is null or lower(vl.title) like :title)
            and (:lessonName is null or lower(l.name) like :lessonName)
            and (:subjectName is null or lower(s.name) like :subjectName)
            """,
            countQuery = """
                    select count(vl) from VideoLesson vl
                    left join vl.lesson l
                    left join l.subject s
                    where vl.active = true
                    and (:title is null or lower(vl.title) like :title)
                    and (:lessonName is null or lower(l.name) like :lessonName)
                    and (:subjectName is null or lower(s.name) like :subjectName)
                    """)
    Page<VideoLesson> searchVideoLessons(@Param("title") String title,
                                         @Param("lessonName") String lessonName,
                                         @Param("subjectName") String subjectName,
                                         Pageable pageable);
}
