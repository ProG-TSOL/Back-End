package com.backend.prog.domain.member.dto;

import com.backend.prog.domain.member.domain.Provider;

import java.util.List;

public class MemberDto {
    public record Response(Integer id, String email, Provider provider, String name, String nickName, String description, String imgUrl, List<MemberTechDto.Response> memberTechList) {
    }

    public record SimpleResponse(Integer id, String name, String nickName, String imgUrl, List<MemberTechDto.Response> memberTechList) {
    }
}
