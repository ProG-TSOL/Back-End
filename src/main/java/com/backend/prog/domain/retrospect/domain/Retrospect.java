package com.backend.prog.domain.retrospect.domain;


import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@ToString(exclude = {"project", "member", "kptCode"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "retrospect")
public class Retrospect extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "retrospect_id")
    @Comment("회고ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @Comment("프로젝트ID")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @Comment("멤버ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kpt_code")
    @Comment("KPT 코드")
    private CodeDetail kptCode;

    @Comment("주차")
    private Integer week;

    @Lob
    @Comment("내용")
    private String content;

    @Builder
    private Retrospect(Project project, Member member, CodeDetail kptCode, Integer week, String content) {
        this.project = project;
        this.member = member;
        this.kptCode = kptCode;
        this.week = week;
        this.content = content;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
