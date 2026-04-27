package com.example.bolalarakademiyasi.repository;

import com.example.bolalarakademiyasi.entity.PageBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PageBlockRepository extends JpaRepository<PageBlock, UUID> {
}
