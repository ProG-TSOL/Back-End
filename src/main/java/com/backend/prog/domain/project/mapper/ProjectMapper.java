package com.backend.prog.domain.project.mapper;

import com.backend.prog.domain.manager.dto.CodeDetailDto;
import com.backend.prog.domain.project.dao.LikeRepository;
import com.backend.prog.domain.project.domain.ApplicationStatus;
import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.domain.project.domain.ProjectMemberId;
import com.backend.prog.domain.project.domain.ProjectTotal;
import com.backend.prog.domain.project.dto.ApplicationStatusDto;
import com.backend.prog.domain.project.dto.ProjectDto;
import com.backend.prog.domain.project.dto.ProjectTotalDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectMapper {

    private final LikeRepository likeRepository;

    public Page<ProjectDto.SimpleResponse> entityToSimpleResponse(Page<Project> projects, Integer memberId) {
        return projects.map(project -> ProjectDto.SimpleResponse.builder()
                .id(project.getId())
                .title(project.getTitle())
                .viewCnt(project.getViewCnt())
                .likeCnt(project.getLikeCnt())
                .isLike(
                        likeRepository.existsLikeById(project.getId(), memberId)
                )
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

    public ProjectDto.Response entityToResponse(Project project, Integer memberId, boolean isMember, ApplicationStatus applicationStatus) {
        return ProjectDto.Response.builder()
                .id(project.getId())
                .isMember(isMember)
                .title(project.getTitle())
                .content(project.getContent())
                .startDay(project.getStartDay())
                .viewCnt(project.getViewCnt())
                .likeCnt(project.getLikeCnt())
                .isLike( likeRepository.existsLikeById(project.getId(), memberId))
                .period(project.getPeriod())
                .projectImgUrl(project.getProjectImgUrl())
                .applicationStatus(applicationStatus == null ? null : CodeDetailDto.SampleResponse.builder()
                        .id(applicationStatus.getJobCode().getId())
                        .detailName(applicationStatus.getJobCode().getDetailName())
                        .detailDescription(applicationStatus.getJobCode().getDetailDescription())
                        .imgUrl(applicationStatus.getJobCode().getImgUrl())
                        .isUse(applicationStatus.getJobCode().getIsUse())
                        .build()
                )
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

    public List<ProjectDto.SimpleResponse> entityToSimpleResponseList(List<Project> list, Integer memberId) {
        return list.stream().map(ps -> ProjectDto.SimpleResponse.builder()
                .id(ps.getId())
                .title(ps.getTitle())
                .viewCnt(ps.getViewCnt())
                .likeCnt(ps.getLikeCnt())
                .isLike( likeRepository.existsLikeById(ps.getId(), memberId))
                .projectImgUrl(ps.getProjectImgUrl())
                .statusCode(CodeDetailDto.SampleResponse.builder()
                        .id(ps.getStatusCode().getId())
                        .detailName(ps.getStatusCode().getDetailName())
                        .detailDescription(ps.getStatusCode().getDetailDescription())
                        .imgUrl(ps.getStatusCode().getImgUrl())
                        .isUse(ps.getStatusCode().getIsUse())
                        .build()
                )
                .techCodes(ps.getTechCodes().stream()
                        .map(techCode -> CodeDetailDto.SampleResponse.builder()
                                .id(techCode.getTechCode().getId())
                                .detailName(techCode.getTechCode().getDetailName())
                                .detailDescription(techCode.getTechCode().getDetailDescription())
                                .imgUrl(techCode.getTechCode().getImgUrl())
                                .isUse(techCode.getTechCode().getIsUse())
                                .build()
                        ).toList()
                )
                .total(ps.getProjectTotals().stream()
                        .mapToInt(ProjectTotal::getTotal)
                        .sum()
                )
                .current(ps.getProjectTotals().stream()
                        .mapToInt(ProjectTotal::getCurrent)
                        .sum()
                )
                .build()
        ).toList();
    }

    public List<ProjectDto.HotResponse> entityToHotResponse(List<Project> list) {
        return list.stream().map(hp -> ProjectDto.HotResponse.builder()
                .id(hp.getId())
                .statusCode(CodeDetailDto.SampleResponse.builder()
                        .id(hp.getStatusCode().getId())
                        .detailName(hp.getStatusCode().getDetailName())
                        .detailDescription(hp.getStatusCode().getDetailDescription())
                        .imgUrl(hp.getStatusCode().getImgUrl())
                        .isUse(hp.getStatusCode().getIsUse())
                        .build())
                .title(hp.getTitle())
                .viewCnt(hp.getViewCnt())
                .createdAt(hp.getCreatedAt())
                .build()
        ).toList();
    }
}