package com.backend.prog.domain.project.dao;

import com.backend.prog.domain.project.domain.Like;
import com.backend.prog.domain.project.domain.ProjectMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, ProjectMemberId> {
}
