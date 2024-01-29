package com.backend.prog.domain.project.dao;

import com.backend.prog.domain.project.domain.ProjectMember;
import com.backend.prog.domain.project.domain.ProjectMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRespository extends JpaRepository<ProjectMember, ProjectMemberId> {
}
