package com.backend.prog.domain.project.dao;

import com.backend.prog.domain.project.domain.ProjectCodeDetaliId;
import com.backend.prog.domain.project.domain.ProjectTotal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectTotalRespository extends JpaRepository<ProjectTotal, ProjectCodeDetaliId> {

    @Query(value = "select sum(p.current) as totalCurrent from ProjectTotal p where p.project.id = :projectId")
    Integer countCurrentByProjectId(Long projectId);
}
