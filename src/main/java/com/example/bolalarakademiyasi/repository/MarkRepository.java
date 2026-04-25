package com.example.bolalarakademiyasi.repository;

import com.example.bolalarakademiyasi.entity.Mark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface MarkRepository extends JpaRepository<Mark, UUID> {

    @Query("""
SELECT m
FROM Mark m
WHERE m.id IN (
    SELECT MAX(m2.id)
    FROM Mark m2
    WHERE m2.student.sinf.teacher.id = :teacherId
    GROUP BY m2.student.id
)
ORDER BY m.id DESC
""")
    Page<Mark> findAllByTeacherId(@Param("teacherId") UUID teacherId, Pageable pageable);

    @Query("""
    SELECT m
    FROM Mark m
    WHERE m.id IN (
        SELECT MAX(m2.id)
        FROM Mark m2
        WHERE (:keyword IS NULL OR :keyword = '' OR LOWER(m2.student.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(m2.student.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:sinfId IS NULL OR m2.student.sinf.id = :sinfId)
        GROUP BY m2.student.id
    )
    ORDER BY m.id DESC
""")
    Page<Mark> findAllMark(@Param("keyword") String keyword,
                           @Param("sinfId") UUID sinfId,
                           Pageable pageable);


    @Query("""
SELECT m
FROM Mark m
WHERE m.id IN (
    SELECT MAX(m2.id)
    FROM Mark m2
    WHERE m2.createdBy = :createdBy AND m2.active = true
    GROUP BY m2.student.id
)
ORDER BY m.id DESC
""")
    Page<Mark> findAllByCreatedByAndActiveTrue(String createdBy, Pageable pageable);




    Page<Mark> findAllByStudentIdAndActiveTrue(UUID studentId, Pageable pageable);



    @Query("""
select m.student.id, m.student.firstName,m.student.lastName,
       sum(coalesce(m.totalScore, 0)),
       m.student.imgUrl
from Mark m
where m.active = true
group by m.student.id, m.student.firstName,m.student.lastName, m.student.imgUrl
order by sum(coalesce(m.totalScore, 0)) desc, m.student.lastName asc
""")
    List<Object[]> topAllStudentsByAvg(Pageable pageable);

    @Query("""
select m.student.id, m.student.firstName, m.student.lastName,
       sum(coalesce(m.totalScore, 0)),
       m.student.imgUrl
from Mark m
where m.active = true
  and m.student.sinf.id in :sinfIds
group by m.student.id, m.student.firstName, m.student.lastName, m.student.imgUrl
order by sum(coalesce(m.totalScore, 0)) desc, m.student.lastName asc
""")
    List<Object[]> topStudentsByAvgForGroups(@Param("sinfIds") List<UUID> sinfIds, Pageable pageable);



    List<Mark> findAllByStudentIdAndActiveTrueAndDateBetweenOrderByDateAsc(
            UUID studentId, LocalDate start, LocalDate end
    );

    @Query("""
        select avg(m.totalScore * 1.0)
        from Mark m
        where m.active = true and m.student.id = :studentId
    """)
    Double avgTotalScore(@Param("studentId") UUID studentId);

    @Query("""
        select count(m)
        from Mark m
        where m.active = true and m.student.id = :studentId
    """)
    Long countMarks(@Param("studentId") UUID studentId);



    @Query("""
        select m from Mark m
        where m.active = true
          and m.student.sinf.id = :sinfId
          and m.date < :today
          and (:keyword is null or :keyword = ''
               or lower(m.student.firstName) like lower(concat('%', :keyword, '%'))
               or lower(m.student.lastName) like lower(concat('%', :keyword, '%') ) )
          and (:createdBy is null or m.createdBy = :createdBy)
          and m.id in (
              select max(m2.id) from Mark m2
              where m2.active = true
                and m2.student.sinf.id = :groupId
                and m2.date < :today
              group by m2.student.id, m2.date
          )
        order by m.date desc, m.student.lastName asc
    """)
    Page<Mark> findArchiveMarksByGroup(
            @Param("sinfId") UUID sinfId,
            @Param("today") LocalDate today,
            @Param("keyword") String keyword,
            @Param("createdBy") String createdBy,
            Pageable pageable);



    Page<Mark> findAllByCreatedByAndDateBetweenAndActiveTrue(
            String createdBy,
            LocalDate start,
            LocalDate end,
            Pageable pageable);

    Page<Mark> findAllByStudentIdAndDateBetweenAndActiveTrue(
            UUID studentId,
            LocalDate start,
            LocalDate end,
            Pageable pageable);

    Page<Mark> findAllByDateBetweenAndActiveTrue(
            LocalDate start,
            LocalDate end,
            Pageable pageable);
}
