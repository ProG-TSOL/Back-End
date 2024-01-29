package com.backend.prog.domain.work.dao;


import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.domain.work.dao.custom.WorkRepositoryCustom;
import com.backend.prog.domain.work.domain.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface WorkRepository extends JpaRepository<Work, Long>, WorkRepositoryCustom {

//    List<Work> findAllByProject(Project project);
}
