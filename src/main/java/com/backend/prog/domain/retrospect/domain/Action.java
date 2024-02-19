package com.backend.prog.domain.retrospect.domain;

import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@ToString(exclude = {"project"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "action")
public class Action extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "action_id")
    @Comment("액션ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @Comment("프로젝트ID")
    private Project project;

    @Lob
    @Comment("내용")
    private String content;

    @Comment("주차")
    private Integer week;

    @Builder
    private Action(Project project, String content, Integer week) {
        this.project = project;
        this.content = content;
        this.week = week;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
