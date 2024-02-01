package com.backend.prog.domain.project.domain;

import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@ToString(exclude = {"project", "member", "jobCode"})
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "application_status")
public class ApplicationStatus extends BaseEntity {
    @EmbeddedId
    private ProjectMemberId id;

    @MapsId("projectId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @Comment("프로젝트ID")
    private Project project;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @Comment("멤버ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_code")
    @Comment("직무코드")
    private CodeDetail jobCode;
}
