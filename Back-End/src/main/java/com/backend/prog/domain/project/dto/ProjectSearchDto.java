package com.backend.prog.domain.project.dto;

import com.backend.prog.domain.manager.domain.CodeDetail;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectSearchDto {
    private String teamName;
    private String title;
    private String content;
    private CodeDetail statusCode;

    @QueryProjection
    public ProjectSearchDto(String teamName, String title, String content, CodeDetail statusCode) {
        this.teamName = teamName;
        this.title = title;
        this.content = content;
        this.statusCode = statusCode;
    }
}
