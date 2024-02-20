package com.backend.prog.domain.member.application;

import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.domain.member.dto.MemberDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {

    void signUp(MemberDto.Post memberDto);

    void updateProfile(MemberDto.ProfilePatch memberDto, MultipartFile file);

    MemberDto.Response getMyProfile(Integer id);

    MemberDto.Response getProfile(Integer id);

    MemberDto.DetailResponse getDetailProfile(Integer id);

    void checkEmail(String email);

    void checkNickname(Integer id, String nickname);

    void changePassword(MemberDto.PasswordPatch MemberDto);

    Member getMember(Integer memberId);

    void sendAuthCode(String email);

    String createAuthCode();

    void verifyAuthCode(String email, String authCode);

    void withdrawalMember(HttpServletRequest request, HttpServletResponse response, Integer id);
}