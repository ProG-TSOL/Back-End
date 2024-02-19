package com.backend.prog.domain.manager.dto;

import lombok.Builder;

public class CodeDetailDto {
    public record Request(Integer techCode) {
    }

    @Builder
    public record SampleResponse(Integer id, String detailName, String detailDescription, String imgUrl, Boolean isUse) {

    }
}
