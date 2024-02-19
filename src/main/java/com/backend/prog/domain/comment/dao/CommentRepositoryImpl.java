package com.backend.prog.domain.comment.dao;

import com.backend.prog.domain.comment.domain.QComment;
import com.backend.prog.domain.comment.dto.CommentSimpleDto;
import com.backend.prog.domain.comment.dto.QCommentSimpleDto;
import com.backend.prog.domain.manager.domain.CodeDetail;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.backend.prog.domain.comment.domain.QComment.comment;

public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public CommentRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }


    @Override
    public List<CommentSimpleDto> getComments(CodeDetail findCodeDetail, Long contentId) {
        QComment co = new QComment("co");

        return jpaQueryFactory
                .select(new QCommentSimpleDto(
                                co.id,
                                co.contentCode,
                                co.member,
                                co.parentId,
                                co.contentId,
                                co.content,
                                co.isDeleted,
                                ExpressionUtils.as(
                                        jpaQueryFactory.select(comment.count())
                                                .from(comment)
                                                .where(comment.parentId.eq(co.id)), "children"
                                ),
                                co.createdAt,
                                co.modifiedAt
                        )
                )
                .from(co)
                .where(co.parentId.isNull(), co.contentCode.eq(findCodeDetail), co.contentId.eq(contentId))
                .fetch();
    }
}
