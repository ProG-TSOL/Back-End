package com.backend.prog.domain.attendance.domain;

import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@ToString(exclude = {"project", "member"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "attendance")
public class Attendance extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    @Comment("근태ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @Comment("프로젝트ID")
    private Project project;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @Comment("멤버ID")
    private Member member;

    @Comment("근무일자")
    private LocalDate workingDay;
    @Comment("근무시간")
    private LocalTime workingTime;

    @Builder
    private Attendance(Project project, Member member, LocalDate workingDay, LocalTime workingTime) {
        this.project = project;
        this.member = member;
        this.workingDay = workingDay;
        this.workingTime = workingTime;
    }

    public void plusWorkingTime(Long hours, Long  minutes, Long  seconds) {
        this.workingTime = this.workingTime.plusHours(hours).plusMinutes(minutes).plusSeconds(seconds);
    }
}
