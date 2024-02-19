package com.backend.prog.domain.feed.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KafkaFeedDto {
    private Long projectId;
    private Integer contentsCode;
    private Long contentsId;
    private Integer memberId;

    @Builder
    private KafkaFeedDto(Long projectId, Integer contentsCode, Long contentsId, Integer memberId) {
        this.projectId = projectId;
        this.contentsCode = contentsCode;
        this.contentsId = contentsId;
        this.memberId = memberId;
    }
}
