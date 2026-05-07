package com.example.bolalarakademiyasi.repository;

import com.example.bolalarakademiyasi.entity.VideoProgress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface VideoProgressRepository extends JpaRepository<VideoProgress, UUID> {

    Optional<VideoProgress> findByIdAndActiveTrue(UUID id);

    Optional<VideoProgress> findByStudentIdAndVideoLessonIdAndActiveTrue(UUID studentId, UUID videoLessonId);

    @Query(value = """
            select vp from VideoProgress vp
            left join vp.student st
            left join vp.videoLesson vl
            left join vl.lesson l
            where vp.active = true
            and (:studentName is null
                or lower(st.firstName) like :studentName
                or lower(st.lastName) like :studentName)
            and (:videoTitle is null or lower(vl.title) like :videoTitle)
            and (:lessonName is null or lower(l.name) like :lessonName)
            and (:completed is null or vp.completed = :completed)
            """,
            countQuery = """
                    select count(vp) from VideoProgress vp
                    left join vp.student st
                    left join vp.videoLesson vl
                    left join vl.lesson l
                    where vp.active = true
                    and (:studentName is null
                        or lower(st.firstName) like :studentName
                        or lower(st.lastName) like :studentName)
                    and (:videoTitle is null or lower(vl.title) like :videoTitle)
                    and (:lessonName is null or lower(l.name) like :lessonName)
                    and (:completed is null or vp.completed = :completed)
                    """)
    Page<VideoProgress> searchVideoProgresses(@Param("studentName") String studentName,
                                              @Param("videoTitle") String videoTitle,
                                              @Param("lessonName") String lessonName,
                                              @Param("completed") Boolean completed,
                                              Pageable pageable);
}
