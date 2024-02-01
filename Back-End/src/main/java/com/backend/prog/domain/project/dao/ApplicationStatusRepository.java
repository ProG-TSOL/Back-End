package com.backend.prog.domain.project.dao;

import com.backend.prog.domain.project.domain.ApplicationStatus;
import com.backend.prog.domain.project.domain.ProjectMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationStatusRepository extends JpaRepository<ApplicationStatus, ProjectMemberId> {
}
