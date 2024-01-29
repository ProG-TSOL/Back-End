package com.backend.prog.domain.project.dao;

import com.backend.prog.domain.project.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface ProjectRespository extends JpaRepository<Project, Long>, ProjectRespositoryCustom {
    @Query(value = "select p from Project p where p.isDeleted = false")
    public Page<Project> findAllByDeletedIsFalse(String keyword, Pageable pageable);
}
