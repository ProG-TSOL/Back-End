package com.backend.prog.domain.feed.domain;

import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"project", "contentCode"})
@Table(name = "feed")
public class Feed extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    @Comment("피드ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @Comment("프로젝트ID")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_code")
    @Comment("컨텐츠코드")
    private CodeDetail contentCode;

    @Comment("컨텐츠ID")
    private Long contentId;

    @Comment("멤버ID")
    private Integer memberId;

    @Comment("피드내용")
    private String content;

    @Builder
    private Feed(Project project, CodeDetail contentCode, Long contentId, Integer memberId, String content) {
        this.project = project;
        this.contentCode = contentCode;
        this.contentId = contentId;
        this.memberId = memberId;
        this.content = content;
    }
}
