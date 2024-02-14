package com.backend.prog.domain.project.dao;

import com.backend.prog.domain.project.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectRespositoryCustom {
    Page<Project> getProject(String keyword, Integer techCodes, Integer statusCode, Pageable pageable);

    List<Project> findHotProject();

    List<Project> findMyProjectByStatus(Integer memberId, Integer statusCode);

    List<Project> findAllMySignedProject(Integer memberId);
}
