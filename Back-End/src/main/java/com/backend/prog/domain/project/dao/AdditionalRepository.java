package com.backend.prog.domain.project.dao;

import com.backend.prog.domain.project.domain.Additional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdditionalRepository extends JpaRepository<Additional, Long> {
    @Query(value = "select a from Additional a where a.project.id = :projectId")
    Additional findAllByProjectId(@Param("projectId") Long projectId);

    int countAllByProjectId(Long projectId);

    @Query(value = "select a from Additional a where a.project.id = :projectId and a.isDeleted = false")
    List<Additional> findAllByProjectIdAndIsDeletedFalse(@Param("projectId") Long projectId);

    @Query(value = "select count(a) from Additional a where a.project.id = :projectId and a.isDeleted = false")
    int countAllByProjectIdAndIdDeleteFalse(@Param("projectId") Long projectId);
}
