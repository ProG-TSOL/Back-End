package com.backend.prog.domain.work.dao.custom;

import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.domain.work.domain.Work;

import java.util.List;

public interface WorkRepositoryCustom {
    List<Work> findAllByProject(Project project);
}
