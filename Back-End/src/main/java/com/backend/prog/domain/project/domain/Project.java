package com.backend.prog.domain.project.domain;

import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.project.dto.ProjectDto;
import com.backend.prog.global.common.DeleteEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@ToString(exclude = {"statusCode", "techCodes", "projectTotals", "projectMembers"})
@NoArgsConstructor
@DynamicInsert
@Table(name = "project")
public class Project extends DeleteEntity implements Serializable {
    @Id
    @Column(name = "project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("프로젝트 아이디")
    private Long id;
    @Column(length = 150)
    @Comment("제목")
    private String title;
    @Lob
    @Comment("내용")
    private String content;
    @Comment("프로젝트 시작일")
    private LocalDate startDay;
    @Comment("프로젝트 종료일")
    private LocalDate endDay;
    @Comment("조회수")
    @ColumnDefault("0")
    private Integer viewCnt;
    @Comment("좋아요 수")
    @ColumnDefault("0")
    private Integer likeCnt;
    @Comment("프로젝트 기간")
    private Integer period;
    @Column(length = 255)
    @Comment("프로젝트 이미지 주소")
    private String projectImgUrl;

    @ManyToOne
    @JoinColumn(name = "status_code")
    @Comment("프로젝트 상태 코드")
    private CodeDetail statusCode;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<ProjectTechCode> techCodes = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<ProjectTotal> projectTotals = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<ProjectMember> projectMembers = new ArrayList<>();

    @Builder
    private Project(String title, String content, Integer period, String projectImgUrl) {
        this.title = title;
        this.content = content;
        this.period = period;
        this.projectImgUrl = projectImgUrl;
    }

    public Project(ProjectDto.Post post, String projectImgUrl, CodeDetail statusCode) {
        Optional.of(post.title()).ifPresent(this::setTitle);
        Optional.of(post.content()).ifPresent(this::setContent);
        Optional.of(post.period()).ifPresent(this::setPeriod);
        Optional.of(projectImgUrl).ifPresent(this::setProjectImgUrl);
        Optional.of(statusCode).ifPresent(this::setStatusCode);
    }

    public void updateProject(ProjectDto.Patch patch, String projectImgUrl) {
        Optional.of(patch.title()).ifPresent(this::setTitle);
        Optional.of(patch.content()).ifPresent(this::setContent);
        Optional.of(patch.period()).ifPresent(this::setPeriod);
        Optional.of(projectImgUrl).ifPresent(this::setProjectImgUrl);
    }

    public boolean hasTechCode(ProjectTechCode projectTechCode) {
        return this.techCodes.contains(projectTechCode);
    }

    public void addTotal(ProjectTotal projectTotal) {
        if (!this.projectTotals.contains(projectTotal)) {
            this.projectTotals.add(projectTotal);
        }
    }

    public void addTech(ProjectTechCode projectTechCode) {
        if (!this.techCodes.contains(projectTechCode)) {
            this.techCodes.add(projectTechCode);
        }
    }

    public void startProject(CodeDetail start) {
        this.statusCode = start;
        this.startDay = LocalDate.now();
    }

    public void endProject(CodeDetail end) {
        this.statusCode = end;
        this.endDay = LocalDate.now();
    }

    public void updateEndDate(LocalDate endDay) {
        this.endDay = endDay;
    }

    public void addLike() {
     this.likeCnt += 1;
    }

    public void deleteLike() {
        this.likeCnt -= 1;
    }

    public void addView() {
        this.viewCnt += 1;
    }
}
