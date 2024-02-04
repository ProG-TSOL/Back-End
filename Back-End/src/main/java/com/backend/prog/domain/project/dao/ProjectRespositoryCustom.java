package com.backend.prog.domain.project.dao;

import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.domain.project.dto.ProjectDto;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectRespositoryCustom {
    Page<ProjectDto.SimpleResponse> getProjectDetails(Pageable pageable);

    Page<Project> getProject(String keyword, Integer techCodes, Integer statusCode, Pageable pageable);
}
