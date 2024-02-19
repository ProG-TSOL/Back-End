package com.backend.prog.domain.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeDetailResponse {
    private CodeResponse code;
    private Integer id;
    private String detailName;
    private String detailDescription;
    private String imgUrl;
    private Boolean isUse;
}
