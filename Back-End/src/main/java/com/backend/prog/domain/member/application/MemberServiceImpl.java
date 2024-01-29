package com.backend.prog.domain.member.application;

import com.backend.prog.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    @Override
    public MemberDto.Response getProfile(Integer id) {
        return null;
    }

    @Override
    public MemberDto.SimpleResponse getMiniProfile(Integer id) {
        return null;
    }
}
