package com.backend.prog.domain.project.dto;

import com.backend.prog.domain.manager.dto.CodeDetailDto;
import lombok.Builder;

public class ProjectTotalDto {
    public record Post(Integer jobCode, Integer total, Integer current){

    }

    public record Patch(Integer jobCode, Integer total, Integer current){

    }

    @Builder
    public record Response(){

    }

    @Builder
    public record SimpleResponse(CodeDetailDto.SampleResponse jobCode,
                                 Integer total, Integer current){

    }
}
