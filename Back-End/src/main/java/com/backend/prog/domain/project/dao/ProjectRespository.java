package com.backend.prog.domain.project.dao;

import com.backend.prog.domain.project.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ProjectRespository extends JpaRepository<Project, Long>, ProjectRespositoryCustom {
    @Query(value = "select p from Project p where p.isDeleted = false")
    public Page<Project> findAllByDeletedIsFalse(String keyword, Pageable pageable);

    @Query(value = "select p from Project p " +
            "where p.id in (select pm2.project.id from ProjectMember pm2 where pm2.member.id = :memberId) " +
            "or p.id in (select als.project.id from ApplicationStatus als where als.member.id = :memberId) " +
            "order by p.createdAt desc "
    )
    List<Project> findAllMyProject(Integer memberId);

//    The project I signed up for

}
