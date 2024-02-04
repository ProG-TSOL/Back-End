package com.backend.prog.domain.member.application;

import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.domain.member.dto.MemberDto;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {

    void signUp(MemberDto.Post memberDto);

    void updateProfile(Integer id, MemberDto.ProfilePatch memberDto, MultipartFile file);

    MemberDto.Response getMyProfile(Integer id);

    MemberDto.Response getProfile(String email);

    MemberDto.DetailResponse getDetailProfile(String email);

    void checkEmail(String email);

    void checkNickname(Integer id, String nickname);

    void changePassword(Integer id, MemberDto.PasswordPatch MemberDto);

    Member getMember(Integer memberId);

    void sendAuthCode(String email);

    String createAuthCode();

    void verifyAuthCode(String email, String authCode);
}


