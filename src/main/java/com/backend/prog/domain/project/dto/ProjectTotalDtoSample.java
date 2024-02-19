package com.backend.prog.domain.project.dto;

import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.project.domain.Project;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectTotalDtoSample {
    private Project project;
    private CodeDetail jobCode;
    private Integer total;

    @QueryProjection
    public ProjectTotalDtoSample(Project project, CodeDetail jobCode, Integer total) {
        this.project = project;
        this.jobCode = jobCode;
        this.total = total;
    }
}
