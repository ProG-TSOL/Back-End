package com.backend.prog.domain.work.domain;

import com.backend.prog.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString(exclude = {"work"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "work_checklist")
public class WorkCheckList extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checklist_id")
    @Comment("체크리스트ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_id")
    @Comment("업무ID")
    private Work work;

    @Comment("제목")
    private String title;

    @Comment("완료 여부")
    @ColumnDefault("false")
    private Boolean isFinish;

    @Comment("시작일시")
    private LocalDateTime startedAt;

    @Comment("종료일시")
    private LocalDateTime finishedAt;

    @Builder
    private WorkCheckList(Work work, String title, Boolean isFinish, LocalDateTime startedAt, LocalDateTime finishedAt) {
        this.work = work;
        this.title = title;
        this.isFinish = isFinish;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
    }

    public void checkFinish(Boolean isFinish) {
        this.isFinish = isFinish;
    }
}
