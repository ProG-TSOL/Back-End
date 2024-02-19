package com.backend.prog.domain.board.dto;

import com.backend.prog.domain.board.domain.Board;
import com.backend.prog.domain.board.domain.BoardImage;
import com.backend.prog.domain.member.domain.Member;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class BoardDetailResponse {
    // 회원
    private Integer memberId;
    private String nickname;
    private String imgUrl;

    //포지션
    private String position;

    // 게시글
    private Long boardId;
    private LocalDateTime createdAt;
    private Boolean isDeleted;
    private String title;
    private String content;
    private Integer viewCnt;
    private Boolean isNotice;

    private List<BoardImgResponse> boardImgResponse;

    public BoardDetailResponse toDto(Member member, Board board, List<BoardImage> boardImages, String position) {
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
        this.content = board.getContent();
        this.viewCnt = board.getViewCnt();
        this.isNotice = board.getIsNotice();
        // 게시글 이미지
        if (!boardImages.isEmpty()) {
            boardImgResponse = boardImages.stream()
                    .map(boardImage -> new BoardImgResponse().toDto(boardImage.getId(), boardImage.getImgUrl()))
                    .toList();
        }
        // 댓글

        return this;
    }

}
