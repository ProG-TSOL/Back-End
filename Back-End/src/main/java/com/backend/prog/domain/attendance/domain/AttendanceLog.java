package com.backend.prog.domain.attendance.domain;

import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString(exclude = {"project", "member"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "attendance_log")
public class AttendanceLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_log_id")
    @Comment("근무일지ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @Comment("멤버ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @Comment("프로젝트ID")
    private Project project;

    @Comment("출근일시")
    private LocalDateTime startAt;
    @Comment("퇴근일시")
    private LocalDateTime endAt;

    @Builder
    private AttendanceLog(Member member, Project project, LocalDateTime startAt, LocalDateTime endAt) {
        this.member = member;
        this.project = project;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public void addEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }
}
