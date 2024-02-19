package com.backend.prog.domain.project.dto;

import com.backend.prog.domain.manager.dto.CodeDetailDto;
import com.backend.prog.domain.member.dto.MemberDto;
import lombok.Builder;

public class ApplicationStatusDto {

    @Builder
    public record Response(MemberDto.Response member, CodeDetailDto.SampleResponse jobCode){

    }

    @Builder
    public record SimpleResponse(CodeDetailDto.SampleResponse jobCode){

    }
}
