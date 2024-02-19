package com.backend.prog.domain.project.dto;

import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.manager.dto.CodeDetailSimpleResponse;
import com.backend.prog.domain.member.domain.Member;
import lombok.Data;

@Data
public class ProjectHomeMemberResponse {
    Integer memberId;
    String imgUrl;
    String nickname;
    String gitUsername;
    CodeDetailSimpleResponse jobCode;
    CodeDetailSimpleResponse roleCode;

    public ProjectHomeMemberResponse toDto(Member member, CodeDetail jobCode, CodeDetail roleCode) {
        this.memberId = member.getId();
        this.imgUrl = member.getImgUrl();
        this.nickname = member.getNickname();
        this.gitUsername = member.getGitUsername();
        this.jobCode = new CodeDetailSimpleResponse().toDto(jobCode);
        this.roleCode = new CodeDetailSimpleResponse().toDto(roleCode);
        return this;
    }
}
