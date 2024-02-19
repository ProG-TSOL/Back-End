package com.backend.prog.domain.project.dao;

import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.domain.project.domain.ProjectMember;
import com.backend.prog.domain.project.domain.ProjectMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectMemberRespository extends JpaRepository<ProjectMember, ProjectMemberId> {
    @Query(value = "select pm from ProjectMember pm where pm.project.id = :projectId order by pm.createdAt")
    List<ProjectMember> findAllByProjectId(@Param("projectId") Long projecId);

    @Query(value = "select pm from ProjectMember pm where pm.project = :project and pm.isDeleted = false")
    List<ProjectMember> findAllByProjectAndNotDeleted(Project project);

}
