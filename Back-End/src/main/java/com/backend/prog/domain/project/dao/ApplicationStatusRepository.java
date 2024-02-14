package com.backend.prog.domain.project.dao;

import com.backend.prog.domain.project.domain.ApplicationStatus;
import com.backend.prog.domain.project.domain.ProjectMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicationStatusRepository extends JpaRepository<ApplicationStatus, ProjectMemberId> {
    @Query(value = "select a from ApplicationStatus a " +
            "where a.project.id = :projectId " +
            "order by a.createdAt desc ")
    List<ApplicationStatus> findAllByProjectMember(@Param("projectId") Long projectId);
}
