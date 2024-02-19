package com.backend.prog.domain.work.domain;

import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

@Entity
@Getter
@ToString(exclude = {"project", "producerId", "statusCode", "priorityCode", "consumerId"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "work")
public class Work extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_id")
    @Comment("업무ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @Comment("프로젝트ID")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @Comment("업무신청자ID")
    private Member producerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_code")
    @Comment("업무상태코드")
    private CodeDetail statusCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_code")
    @Comment("업무구분코드")
    private CodeDetail typeCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priority_code")
    @Comment("우선순위코드")
    private CodeDetail priorityCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consumer_id")
    @Comment("업무담당자ID")
    private Member consumerId;


    @Column(length = 150)
    @Comment("제목")
    private String title;

    @Lob
    @Comment("내용")
    private String content;

    @Comment("시작일")
    private LocalDate startDay;

    @Comment("종료일")
    private LocalDate endDay;

    @Builder
    private Work(Project project, Member producerId, CodeDetail statusCode, CodeDetail typeCode, CodeDetail priorityCode,
                 Member consumerId, String title, String content, LocalDate startDay, LocalDate endDay) {
        this.project = project;
        this.producerId = producerId;
        this.statusCode = statusCode;
        this.typeCode = typeCode;
        this.priorityCode = priorityCode;
        this.consumerId = consumerId;
        this.title = title;
        this.content = content;
        this.startDay = startDay;
        this.endDay = endDay;
    }

    public void updateWork(CodeDetail statusCode,CodeDetail typeCode, CodeDetail priorityCode,
                           Member consumerId, String title, String content, LocalDate startDay, LocalDate endDay) {
        if (statusCode != null) {this.statusCode = statusCode;}
        if (typeCode != null) {this.typeCode = typeCode;}
        if (priorityCode != null) {this.priorityCode = priorityCode;}
        if (consumerId != null) {this.consumerId = consumerId;}
        if (StringUtils.hasText(title)) {this.title = title;}
        if (StringUtils.hasText(content)) {this.content = content;}
        if (startDay != null) {this.startDay = startDay;}
        if (endDay != null) {this.endDay = endDay;}
    }

    public void updateWorkStatus(CodeDetail statusCode) {
        if (statusCode != null) {
            this.statusCode = statusCode;
        }
    }

}
