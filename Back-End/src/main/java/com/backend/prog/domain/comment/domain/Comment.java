package com.backend.prog.domain.comment.domain;

import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.global.common.DeleteEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment")
public class Comment extends DeleteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    @org.hibernate.annotations.Comment("댓글ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_code")
    @org.hibernate.annotations.Comment("컨텐츠코드")
    private CodeDetail contentCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @org.hibernate.annotations.Comment("멤버ID")
    private Member member;

    @org.hibernate.annotations.Comment("댓글부모ID")
    private Long parentId;

    @org.hibernate.annotations.Comment("컨텐츠ID")
    private Long contentId;

    @Column(length = 300)
    @org.hibernate.annotations.Comment("내용")
    private String content;

    @Builder
    private Comment(CodeDetail contentCode, Member member, Long parentId, Long contentId, String content) {
        this.contentCode = contentCode;
        this.member = member;
        this.parentId = parentId;
        this.contentId = contentId;
        this.content = content;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
