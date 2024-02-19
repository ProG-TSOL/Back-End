package com.backend.prog.domain.manager.dto;


import com.backend.prog.domain.manager.domain.CodeDetail;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CodeDetailSimpleResponse {
    private Integer id;
    private String detailName;
    private String detailDescription;
    private String imgUrl;

    public CodeDetailSimpleResponse toDto(CodeDetail codeDetail) {
        this.id = codeDetail.getId();
        this.detailName = codeDetail.getDetailName();
        this.detailDescription = codeDetail.getDetailDescription();
        this.imgUrl = codeDetail.getImgUrl();
        return this;
    }
}
