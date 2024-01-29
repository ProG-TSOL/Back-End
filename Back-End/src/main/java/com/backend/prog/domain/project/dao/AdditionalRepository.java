package com.backend.prog.domain.project.dao;

import com.backend.prog.domain.project.domain.Additional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdditionalRepository extends JpaRepository<Additional, Long> {
    @Query(value = "select a from Additional a where a.project.id = :projectId")
    List<Additional> findAllByProjectId(@Param("projectId") Long projectId);

    int countAllByProjectId(Long projectId);
}
