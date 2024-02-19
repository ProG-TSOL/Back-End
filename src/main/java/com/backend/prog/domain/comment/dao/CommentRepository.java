package com.backend.prog.domain.comment.dao;

import com.backend.prog.domain.comment.domain.Comment;
import com.backend.prog.domain.manager.domain.CodeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    @Query("select c " +
            "from Comment c " +
            "where c.contentCode = :codeDetail " +
            "and c.contentId = :contentId " +
            "and c.isDeleted = false ")
    List<Comment> inquriyForDeletion(@Param("codeDetail") CodeDetail codeDetail, @Param("contentId") Long contentId);

    @Query(value = "select c from Comment c " +
            "where c.parentId = :parentId ")
    List<Comment> findByParentId(@Param("parentId") Long parentId);
}
