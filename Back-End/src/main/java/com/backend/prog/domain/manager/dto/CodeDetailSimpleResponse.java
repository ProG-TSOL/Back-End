package com.backend.prog.domain.manager.dto;


import com.backend.prog.domain.manager.domain.CodeDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CodeDetailSimpleResponse {

    private Integer id;
    private String name;
    private String description;
    private String imgUrl;

    @Builder
    private CodeDetailSimpleResponse(Integer id, String name, String description, String imgUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
    }

    public static CodeDetailSimpleResponse toDto(CodeDetail codeDetail) {
        return CodeDetailSimpleResponse.builder()
                .id(codeDetail.getId())
                .name(codeDetail.getDetailName())
                .description(codeDetail.getDetailDescription())
                .imgUrl(codeDetail.getImgUrl())
                .build();
    }
}
