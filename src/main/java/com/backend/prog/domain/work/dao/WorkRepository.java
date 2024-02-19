package com.backend.prog.domain.work.dao;


import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.domain.work.dao.custom.WorkRepositoryCustom;
import com.backend.prog.domain.work.domain.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkRepository extends JpaRepository<Work, Long>, WorkRepositoryCustom {
    @Query("select w from Work w where w.project = :project and w.title like %:title%")
    List<Work> findAllByTitle(@Param("project") Project project, @Param("title") String title);

    @Query("select count(*) from Work w where w.project.id = :projectId and w.consumerId.id = :memberId")
    Integer findCountMyWork(@Param("projectId") Long projectId, @Param("memberId") Integer memberId);
}
