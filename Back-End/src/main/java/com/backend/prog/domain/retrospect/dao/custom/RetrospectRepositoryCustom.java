package com.backend.prog.domain.retrospect.dao.custom;

import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.domain.retrospect.domain.Retrospect;

import java.util.List;

public interface RetrospectRepositoryCustom {
    List<Retrospect> getLatestRetrospects(Project project);
}
