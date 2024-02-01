package com.backend.prog.domain.member.application;

import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.domain.member.dto.MemberDto;

public interface MemberService {

    MemberDto.Response getProfile(Integer id);

    MemberDto.DetailResponse getDetailProfile(Integer id);

    boolean checkNickname(String nickName);

    void changePassword(Integer id, MemberDto.PasswordPatch MemberDto);

    Member getMember(Integer memberId);
}


