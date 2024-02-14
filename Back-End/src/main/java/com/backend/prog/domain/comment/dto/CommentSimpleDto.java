package com.backend.prog.domain.comment.dto;

import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.member.domain.Member;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentSimpleDto {

    private Long id;

    private CodeDetail contentCode;

    private Member member;

    private Long parentId;

    private Long contentId;

    private String content;

    private boolean isDeleted;

    private Long children;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    @QueryProjection
    public CommentSimpleDto(Long id, CodeDetail contentCode, Member member, Long parentId, Long contentId, String content, boolean isDeleted, Long children, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.contentCode = contentCode;
        this.member = member;
        this.parentId = parentId;
        this.contentId = contentId;
        this.content = content;
        this.isDeleted = isDeleted;
        this.children = children;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
