package com.backend.prog.domain.feed.dao;

import com.backend.prog.domain.feed.domain.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long>{
    @Query(value = "select f from Feed f where f.memberId = :memberId and f.project.id = :projectId")
    List<Feed> findByProjectMember(@Param("memberId") Integer memberId, @Param("projectId") Long projectId);
}
