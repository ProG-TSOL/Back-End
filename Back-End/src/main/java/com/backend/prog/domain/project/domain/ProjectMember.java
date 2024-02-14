package com.backend.prog.domain.project.domain;

import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.global.common.DeleteEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "project_member")
public class ProjectMember extends DeleteEntity {
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

    @OneToOne
    @JoinColumn(name = "job_code")
    @Comment("직무 코드")
    private CodeDetail jobCode;

    @OneToOne
    @JoinColumn(name = "role_code")
    @Comment("역할 코드")
    private CodeDetail roleCode;

    @Builder
    private ProjectMember(ProjectMemberId id, Member member, Project project, CodeDetail jobCode, CodeDetail roleCode){
        this.member = member;
        this.project = project;
        this.jobCode = jobCode;
        this.roleCode = roleCode;
    }

    public void updateJob(CodeDetail updateJob) {
        this.jobCode = updateJob;
    }
}
