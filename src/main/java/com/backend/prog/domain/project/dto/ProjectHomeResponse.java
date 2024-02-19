package com.backend.prog.domain.project.dto;

import com.backend.prog.domain.project.domain.Project;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectHomeResponse {
    Long projectId;
    String title;
    LocalDate startDay;
    LocalDate endDay;
    Integer progress;
    Integer myWorkCnt;

    public ProjectHomeResponse toDto(Project project, Integer progress, Integer myWorkCnt) {
        this.projectId = project.getId();
        this.title = project.getTitle();
        this.startDay = project.getStartDay();
        this.endDay = project.getEndDay();
        this.progress = progress;
        this.myWorkCnt = myWorkCnt;
        return this;
    }

}
