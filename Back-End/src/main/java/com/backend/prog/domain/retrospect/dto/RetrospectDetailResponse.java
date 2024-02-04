package com.backend.prog.domain.retrospect.dto;

import com.backend.prog.domain.retrospect.domain.Retrospect;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RetrospectDetailResponse {

    private Long retrospectId;

    // 회원
    private Integer memberId;
    private String nickname;
    private String imgUrl;

    // KPT 코드
    private Integer kptCode;
    private String kptCodeName;

    private String content;

    public RetrospectDetailResponse toDto(Retrospect entity){
        this.retrospectId = entity.getId();
        this.memberId = entity.getMember().getId();
        this.nickname = entity.getMember().getNickname();
        this.imgUrl = entity.getMember().getImgUrl();
        this.kptCode = entity.getKptCode().getId();
        this.kptCodeName = entity.getKptCode().getDetailName();
        this.content = entity.getContent();
        return this;
    }
}
