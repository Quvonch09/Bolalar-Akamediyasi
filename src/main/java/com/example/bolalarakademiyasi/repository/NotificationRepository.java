package com.example.bolalarakademiyasi.repository;

import com.example.bolalarakademiyasi.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findAllByParentId(UUID parentId);
    List<Notification> findAllByStudentId(UUID studentId);

    @Query(value = """
    select count(*) from notification n
                    where n.parent_id = ?1 and n.is_read = false
    """, nativeQuery = true)
    long countByParentIdAndReadFalse(UUID parentId);

    @Query(value = """
    select count(*) from notification n
                    where n.student_id = ?1 and n.is_read = false
    """, nativeQuery = true)
    long countByStudentIdAndReadFalse(UUID studentId);

}
