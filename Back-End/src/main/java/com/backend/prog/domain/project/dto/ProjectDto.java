package com.backend.prog.domain.project.dto;

import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.manager.dto.CodeDetailDto;
import com.backend.prog.domain.manager.dto.CodeDetailResponse;
import com.backend.prog.domain.project.domain.ApplicationStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ProjectDto {
    public record Post(String title, String content, Integer period,
                       List<CodeDetailDto.Request> totechList, List<ProjectTotalDto.Post> totalList){
    }

    public record Patch(String title, String content, Integer period,
                        List<CodeDetailDto.Request> totechList, List<ProjectTotalDto.Patch> totalList){

    }

    @Builder
    public record Response(Long id, boolean isMember, String title, String content, LocalDate startDay, Integer viewCnt,
                           Integer likeCnt, Integer isLike, Integer period, String projectImgUrl, CodeDetailDto.SampleResponse applicationStatus,
                           CodeDetailDto.SampleResponse statusCode, List<CodeDetailDto.SampleResponse> techCodes, List<ProjectTotalDto.SimpleResponse> projectTotals){

    }

    @Builder
    public record SimpleResponse(Long id, String title, Integer viewCnt, Integer likeCnt, Integer isLike, String projectImgUrl, CodeDetailDto.SampleResponse statusCode,
                                 List<CodeDetailDto.SampleResponse> techCodes, Integer total, Integer current){

    }
    @Builder
    public record HotResponse(Long id, CodeDetailDto.SampleResponse statusCode, String title, Integer viewCnt, LocalDateTime createdAt){

    }
}
