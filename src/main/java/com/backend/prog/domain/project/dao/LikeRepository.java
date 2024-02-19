package com.backend.prog.domain.project.dao;

import com.backend.prog.domain.project.domain.Like;
import com.backend.prog.domain.project.domain.ProjectMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<Like, ProjectMemberId> {

    @Query(value = "select count (l.id) from Like l where l.project.id = :projectId and l.member.id = :memberId")
    Integer existsLikeById(@Param("projectId") Long projectId, @Param("memberId") Integer memberId);
}
