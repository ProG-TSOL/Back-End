package com.backend.prog.domain.member.application;

import com.backend.prog.domain.member.dto.MemberDto;

public interface MemberService {

    MemberDto.Response getProfile(Integer id);

    MemberDto.SimpleResponse getMiniProfile(Integer id);
}


