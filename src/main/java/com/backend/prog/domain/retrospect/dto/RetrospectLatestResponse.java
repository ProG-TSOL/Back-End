package com.backend.prog.domain.retrospect.dto;

import com.backend.prog.domain.retrospect.domain.Retrospect;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RetrospectLatestResponse {

    private Long retrospectId;
    private Long projectId;
    private Integer memberId;
    private Integer kptCode;
    private String kptCodeName;
    private Integer week;
    private String content;
    private LocalDateTime createdAt;

    public RetrospectLatestResponse toDto(Retrospect entity){
        this.retrospectId = entity.getId();
        this.projectId = entity.getProject().getId();
        this.memberId = entity.getMember().getId();
        this.kptCode = entity.getKptCode().getId();
        this.kptCodeName = entity.getKptCode().getDetailName();
        this.week = entity.getWeek();
        this.content = entity.getContent();
        this.createdAt = entity.getCreatedAt();
        return this;
    }
}
