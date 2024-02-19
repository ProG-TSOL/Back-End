package com.backend.prog.domain.board.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class BoardImgResponse {
    private Long imgId;
    private String imgUrl;

    public BoardImgResponse toDto(Long imgId, String imgUrl) {
        this.imgId = imgId;
        this.imgUrl = imgUrl;
        return this;
    }
}
