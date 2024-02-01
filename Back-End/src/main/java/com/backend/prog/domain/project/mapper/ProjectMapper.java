package com.backend.prog.domain.project.mapper;

import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.manager.dto.CodeDetailDto;
import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.domain.project.domain.ProjectTotal;
import com.backend.prog.domain.project.dto.ProjectDto;
import com.backend.prog.domain.project.dto.ProjectTotalDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class ProjectMapper {

    public Page<ProjectDto.SimpleResponse> entityToSimpleResponse(Page<Project> projects){
        return projects.map(project -> ProjectDto.SimpleResponse.builder()
                .id(project.getId())
                .title(project.getTitle())
                .projectImgUrl(project.getProjectImgUrl())
                .statusCode(CodeDetailDto.SampleResponse.builder()
                        .id(project.getStatusCode().getId())
                        .detailName(project.getStatusCode().getDetailName())
                        .detailDescription(project.getStatusCode().getDetailDescription())
                        .imgUrl(project.getStatusCode().getImgUrl())
                        .isUse(project.getStatusCode().getIsUse())
                        .build())
                .techCodes(project.getTechCodes().stream()
                        .map(techCode -> CodeDetailDto.SampleResponse.builder()
                                .id(techCode.getTechCode().getId())
                                .detailName(techCode.getTechCode().getDetailName())
                                .detailDescription(techCode.getTechCode().getDetailDescription())
                                .imgUrl(techCode.getTechCode().getImgUrl())
                                .isUse(techCode.getTechCode().getIsUse())
                                .build()
                        ).toList()
                )
                .total(project.getProjectTotals().stream()
                        .mapToInt(ProjectTotal::getTotal)
                        .sum()
                )
                .current(project.getProjectTotals().stream()
                        .mapToInt(ProjectTotal::getCurrent)
                        .sum()
                )
                .build());
    }

    public ProjectDto.Response entityToResponse(Project project, boolean isMember) {
        return ProjectDto.Response.builder()
                .id(project.getId())
                .isMember(isMember)
                .title(project.getTitle())
                .content(project.getContent())
                .startDay(project.getStartDay())
                .viewCnt(project.getViewCnt())
                .likeCnt(project.getLikeCnt())
                .period(project.getPeriod())
                .projectImgUrl(project.getProjectImgUrl())
                .statusCode(CodeDetailDto.SampleResponse.builder()
                        .id(project.getStatusCode().getId())
                        .detailName(project.getStatusCode().getDetailName())
                        .detailDescription(project.getStatusCode().getDetailDescription())
                        .imgUrl(project.getStatusCode().getImgUrl())
                        .isUse(project.getStatusCode().getIsUse())
                        .build())
                .techCodes(project.getTechCodes().stream()
                        .map(techCode -> CodeDetailDto.SampleResponse.builder()
                                .id(techCode.getTechCode().getId())
                                .detailName(techCode.getTechCode().getDetailName())
                                .detailDescription(techCode.getTechCode().getDetailDescription())
                                .imgUrl(techCode.getTechCode().getImgUrl())
                                .isUse(techCode.getTechCode().getIsUse())
                                .build()
                        ).toList()
                )
                .projectTotals(project.getProjectTotals().stream()
                        .map(projectTotal -> ProjectTotalDto.SimpleResponse.builder()
                                .jobCode(CodeDetailDto.SampleResponse.builder()
                                        .id(projectTotal.getJobCode().getId())
                                        .detailName(projectTotal.getJobCode().getDetailName())
                                        .detailDescription(projectTotal.getJobCode().getDetailDescription())
                                        .imgUrl(projectTotal.getJobCode().getImgUrl())
                                        .isUse(projectTotal.getJobCode().getIsUse())
                                        .build()
                                )
                                .total(projectTotal.getTotal())
                                .current(projectTotal.getCurrent())
                                .build()
                        ).toList()
                ).build();
    }
}
