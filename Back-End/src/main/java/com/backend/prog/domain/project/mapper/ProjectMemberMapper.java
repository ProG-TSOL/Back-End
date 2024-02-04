package com.backend.prog.domain.project.mapper;

import com.backend.prog.domain.manager.dto.CodeDetailDto;
import com.backend.prog.domain.member.dto.MemberDto;
import com.backend.prog.domain.project.domain.ProjectMember;
import com.backend.prog.domain.project.dto.ProjectMemberDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectMemberMapper {

    public List<ProjectMemberDto.Response> entityToResponse(List<ProjectMember> projectMember){
        return projectMember.stream().map(pm -> ProjectMemberDto.Response.builder()
                        .member(MemberDto.Response.builder()
                                .id(pm.getMember().getId())
                                .nickname(pm.getMember().getNickname())
                                .imgUrl(pm.getMember().getImgUrl())
                                .build()
                        )
                        .jobCode(CodeDetailDto.SampleResponse.builder()
                                .id(pm.getJobCode().getId())
                                .detailName(pm.getJobCode().getDetailName())
                                .detailDescription(pm.getJobCode().getDetailDescription())
                                .imgUrl(pm.getJobCode().getImgUrl())
                                .isUse(pm.getJobCode().getIsUse())
                                .build()
                        )
                        .build()
                ).toList();
    }
}
