package com.backend.prog.domain.project.dao;

import com.backend.prog.domain.project.domain.ProjectCodeDetaliId;
import com.backend.prog.domain.project.domain.ProjectTechCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectTechCodeRespository extends JpaRepository<ProjectTechCode, ProjectCodeDetaliId> {
}
