package com.backend.prog.domain.retrospect.dao.custom;

import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.domain.retrospect.domain.Action;

import java.util.List;

public interface ActionRepositoryCustom {

    List<Action> findLatestAction(Project project);
}
