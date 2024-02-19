package com.backend.prog.domain.feed.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FeedListResponse {
    private Long feedId;
    private Integer contentsCode;
    private Long contentsId;
    private String memberImgUrl;
    private String feedContent;

    @Builder
    private FeedListResponse(Long feedId, Integer contentsCode, Long contentsId, String memberImgUrl, String feedContent) {
        this.feedId = feedId;
        this.contentsCode = contentsCode;
        this.contentsId = contentsId;
        this.memberImgUrl = memberImgUrl;
        this.feedContent = feedContent;
    }
}
