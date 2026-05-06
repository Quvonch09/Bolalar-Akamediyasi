package com.example.bolalarakademiyasi.repository;

import com.example.bolalarakademiyasi.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, UUID> {
    Optional<Subject> findByIdAndActiveTrue(UUID id);


    @Query(value = """
        select s.* from subject s
        where s.active = true
        and (:name is null or LOWER(s.name) like LOWER(CONCAT('%', :name, '%')))
        and (:description is null or LOWER(s.description) like LOWER(CONCAT('%', :description, '%')))
        order by s.created_at desc 
        """, nativeQuery = true)
    Page<Subject> searchSubject(@Param("name") String name,
                                @Param("description") String description,
                                PageRequest pageable);



}
