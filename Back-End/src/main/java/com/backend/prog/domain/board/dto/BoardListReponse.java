package com.backend.prog.domain.board.dto;

import com.backend.prog.domain.board.domain.Board;
import com.backend.prog.domain.member.domain.Member;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
public class BoardListReponse {
    // 작성자 member
    private Integer memberId;
    private String nickname;
    private String imgUrl;

    //포지션
    private String position;

    // 게시글ID, 생성일, 제목, 공지여부, 조회수
    private Long boardId;
    private LocalDateTime createdAt;
    private Boolean isDeleted;
    private String title;
    private Integer viewCnt;
    private Boolean isNotice;

    public BoardListReponse toDto(Member member, Board board, String position) {
        // 회원
        this.memberId = member.getId();
        this.nickname = member.getNickname();
        this.imgUrl = member.getImgUrl();
        //포지션
        this.position = position;
        // 게시글
        this.boardId = board.getId();
        this.createdAt = board.getCreatedAt();
        this.isDeleted = board.isDeleted();
        this.title = board.getTitle();
        this.viewCnt = board.getViewCnt();
        this.isNotice = board.getIsNotice();
        return this;
    }

}
