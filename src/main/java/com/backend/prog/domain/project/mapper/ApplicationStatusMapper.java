package com.backend.prog.domain.project.mapper;

import com.backend.prog.domain.manager.dto.CodeDetailDto;
import com.backend.prog.domain.member.dto.MemberDto;
import com.backend.prog.domain.project.domain.ApplicationStatus;
import com.backend.prog.domain.project.dto.ApplicationStatusDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationStatusMapper {

    public List<ApplicationStatusDto.Response> entityToSimpleResponses(List<ApplicationStatus> list){
        return list.stream().map(as -> ApplicationStatusDto.Response.builder()
                .member(MemberDto.Response.builder()
                        .id(as.getMember().getId())
                        .nickname(as.getMember().getNickname())
                        .imgUrl(as.getMember().getImgUrl())
                        .build())
                .jobCode(CodeDetailDto.SampleResponse.builder()
                        .id(as.getJobCode().getId())
                        .detailName(as.getJobCode().getDetailName())
                        .detailDescription(as.getJobCode().getDetailDescription())
                        .imgUrl(as.getJobCode().getImgUrl())
                        .isUse(as.getJobCode().getIsUse())
                        .build())
                .build()
        ).toList();
    }
}
