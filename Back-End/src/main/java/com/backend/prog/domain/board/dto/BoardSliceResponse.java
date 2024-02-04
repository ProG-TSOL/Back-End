package com.backend.prog.domain.board.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class BoardSliceResponse {

    // 회원, 게시글, 다음페이지 여부, 마지막 게시글ID
    private Boolean hasNext;
    private Long lastBoardId;
    private List<BoardListReponse> boards;

    public BoardSliceResponse toDto(List<BoardListReponse> boards, Boolean hasNext, Long lastBoardId) {
        this.hasNext = hasNext;
        this.lastBoardId = lastBoardId;
        this.boards = boards;
        return this;
    }

}
