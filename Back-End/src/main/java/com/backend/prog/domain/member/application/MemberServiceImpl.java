package com.backend.prog.domain.member.application;

import com.backend.prog.domain.member.dao.MemberRepository;
import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.member.dao.MemberTechRepository;
import com.backend.prog.domain.member.domain.MemberTech;
import com.backend.prog.domain.member.dto.MemberDto;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import com.backend.prog.domain.member.dto.MemberTechDto;
import com.backend.prog.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    private final MemberTechRepository memberTechRepository;

    private final PasswordEncoder passwordEncoder;

    private final String[] PROHIBITED_NICKNAME = {"null", "undefined"};

    @Override
    @Transactional(readOnly = true)
    public MemberDto.Response getProfile(Integer id) {
        Member member = memberRepository.findByIdAndDeletedNot(id).orElseThrow(() -> new CommonException(ExceptionEnum.MEMBER_NOT_FOUND));

        return MemberDto.Response.builder()
                .id(member.getId())
                .nickName(member.getNickname())
                .imgUrl(member.getImgUrl())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDto.DetailResponse getDetailProfile(Integer id) {
        Member member = memberRepository.findByIdAndDeletedNot(id).orElseThrow(() -> new CommonException(ExceptionEnum.MEMBER_NOT_FOUND));
        List<MemberTech> memberTechList = memberTechRepository.findAllByOnlyId(id);
        List<MemberTechDto.Response> memberTechDtoResponseList = new ArrayList<>();

        for(MemberTech memberTech : memberTechList) {
            CodeDetail codeDetail = memberTech.getTechCode();

            memberTechDtoResponseList.add(
                    MemberTechDto.Response.builder()
                            .id(codeDetail.getId())
                            .name(codeDetail.getDetailName())
                            .description(codeDetail.getDetailDescription())
                            .techImgUrl(codeDetail.getImgUrl())
                            .techLevel(memberTech.getTechLevel())
                            .build()
            );
        }


        return MemberDto.DetailResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .nickName(member.getNickname())
                .description(member.getDescription())
                .memberTechList(memberTechDtoResponseList)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkNickname(String nickName) {
        for(String pn : PROHIBITED_NICKNAME) {
            if(nickName.equalsIgnoreCase(pn)) {
                return false;
            }
        }

        return memberRepository.findByNickname(nickName).isPresent();
    }

    @Override
    public void changePassword(Integer id, MemberDto.PasswordPatch memberDto) {
        Member member = memberRepository.findByIdAndDeletedNot(id).orElseThrow(() -> new CommonException(ExceptionEnum.MEMBER_NOT_FOUND));

        if(!passwordEncoder.matches(memberDto.originPassword(), member.getPassword())) {
            throw new CommonException(ExceptionEnum.FAIL_AUTH_PASSWORD);
        }
        else if(memberDto.originPassword().equals(memberDto.updatePassword()) || !memberDto.updatePassword().equals(memberDto.updatePasswordCheck())) {
            throw new CommonException(ExceptionEnum.INVALID_MEMBER_DATA);
        }

        member.changePassword(passwordEncoder.encode(memberDto.updatePassword()));
    }

    @Override
    public Member getMember(Integer memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
    }
}