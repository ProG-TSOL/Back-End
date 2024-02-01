package com.backend.prog.domain.project.dto;

import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.manager.dto.CodeDetailDto;
import com.backend.prog.domain.manager.dto.CodeDetailResponse;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

public class ProjectDto {

    public record Post(String title, String content, Integer period,
                       List<CodeDetailDto.Request> totechList, List<ProjectTotalDto.Post> totalList){
    }

    public record Patch(String title, String content, Integer period,
                        List<CodeDetailDto.Request> totechList, List<ProjectTotalDto.Patch> totalList){

    }

    @Builder
    public record Response(Long id, boolean isMember, String title, String content, LocalDate startDay, Integer viewCnt, Integer likeCnt, Integer period, String projectImgUrl,
                           CodeDetailDto.SampleResponse statusCode, List<CodeDetailDto.SampleResponse> techCodes, List<ProjectTotalDto.SimpleResponse> projectTotals){

    }

    @Builder
    public record SimpleResponse(Long id, String title, String projectImgUrl, CodeDetailDto.SampleResponse statusCode,
                                 List<CodeDetailDto.SampleResponse> techCodes, Integer total, Integer current){

    }
}
