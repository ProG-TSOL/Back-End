package com.backend.prog.domain.board.domain;

import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.global.common.DeleteEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"project", "member"})
@DynamicInsert
@Table(name = "board")
public class Board extends DeleteEntity {
    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("게시글ID")
    private Long id;
    @Column(length = 150)
    @Comment("제목")
    private String title;
    @Lob
    @Comment("내용")
    private String content;
    @Comment("공지여부")
    private Boolean isNotice;
    @Comment("조회수")
    @ColumnDefault("0")
    private Integer viewCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @Comment("프로젝트ID")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @Comment("멤버ID")
    private Member member;

    @Builder
    private Board(String title, String content, Boolean isNotice,
                  Project project, Member member) {
        this.title = title;
        this.content = content;
        this.isNotice = isNotice;
        this.project = project;
        this.member = member;
    }

    public void update(String title, String content, Boolean isNotice) {
        this.title = title;
        this.content = content;
        this.isNotice = isNotice;
    }

    public void addViewCnt() {
        if (this.viewCnt == null) {
            this.viewCnt = 1;
            return;
        }
        this.viewCnt++;
    }

    public void changeNotice() {
        this.isNotice = !this.isNotice;
    }

}
