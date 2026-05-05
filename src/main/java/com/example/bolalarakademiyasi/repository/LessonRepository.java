package com.example.bolalarakademiyasi.repository;

import com.example.bolalarakademiyasi.entity.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, UUID> {

    Optional<Lesson> findByIdAndActiveTrue(UUID id);


    @Query(value = """
            select l.* from lesson l
            join subject s on l.subject_id = s.id
            where l.active = true
            and (:name is null or LOWER(l.name) like LOWER(CONCAT('%', :name, '%')))
            and (:subjectName is null
                or LOWER(s.name) like LOWER(CONCAT('%', :subjectName, '%')))
            order by  l.created_at desc 
            """, nativeQuery = true)
    Page<Lesson> searchLesson(@Param("name") String name,
                              @Param("subjectName") String subjectName,
                              PageRequest pageable);


}
