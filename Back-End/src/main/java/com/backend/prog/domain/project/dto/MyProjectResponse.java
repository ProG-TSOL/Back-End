package com.backend.prog.domain.project.dto;

import com.backend.prog.domain.manager.dto.CodeDetailSimpleResponse;
import com.backend.prog.domain.project.domain.Project;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MyProjectResponse {
    Long projectId;
    String title;
    LocalDate startDay;
    LocalDate endDay;
    String projectImgUrl;
    Integer joinTotal;
    CodeDetailSimpleResponse statusCode;
    Integer progress;

    public MyProjectResponse toDto(Project project, Integer joinTotal, Integer progress) {
        this.projectId = project.getId();
        this.title = project.getTitle();
        this.startDay = project.getStartDay();
        this.endDay = project.getEndDay();
        this.projectImgUrl = project.getProjectImgUrl();
        this.statusCode = new CodeDetailSimpleResponse().toDto(project.getStatusCode());
        this.joinTotal = joinTotal;
        this.progress = progress;
        return this;
    }
}
