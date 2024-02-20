package com.backend.prog.domain.member.application;

import com.backend.prog.domain.member.dao.MemberRepository;
import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.domain.member.domain.Role;
import com.backend.prog.domain.member.dto.MemberDto;
import com.backend.prog.global.S3.S3Uploader;
import com.backend.prog.global.auth.dao.EmailAuthRepository;
import com.backend.prog.global.auth.service.EmailService;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import com.backend.prog.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final JwtUtil jwtUtil;

    private final S3Uploader s3Uploader;

    private final EmailService emailService;

    private final MemberRepository memberRepository;

    private final EmailAuthRepository emailAuthRepository;

    private final PasswordEncoder passwordEncoder;

    private static final String[] PROHIBITED_NICKNAME = {"null", "undefined", "탈퇴회원"};

    private static final String BASIC_PROFILE_IMAGE_NAME = "BasicImg";

    private static final String BASIC_PROFILE_IMAGE_URL = "https://ssafy-prog-bucket.s3.amazonaws.com/fkQJvXRM4AEViD9oNA-Yj_nhQQ4upD8Uno079rmlltCitJJGfmLDI_3QoG_YFtW9jSsx51e65hQ0JdQ6AH0wxA.webp";

    @Override
    public void signUp(MemberDto.Post memberDto) {
        checkNickname(0, memberDto.nickname());

        if(memberDto.name().getBytes().length < 4 || memberDto.name().getBytes().length > 30) {
            throw new CommonException(ExceptionEnum.INVALID_MEMBER_DATA);
        }

        checkEmail(memberDto.email());

        if(!memberDto.password().equals(memberDto.passwordCheck())) {
            throw new CommonException(ExceptionEnum.INVALID_MEMBER_DATA);
        }

        Member member = Member.builder()
                .email(memberDto.email())
                .password(passwordEncoder.encode(memberDto.password()))
                .name(memberDto.name())
                .nickname(memberDto.nickname())
                .build();

        member.addRole(Role.USER);

        member.updateProfileImg(BASIC_PROFILE_IMAGE_NAME, BASIC_PROFILE_IMAGE_URL);

        memberRepository.save(member);
    }

    @Override
    public void updateProfile(MemberDto.ProfilePatch memberDto, MultipartFile file) {
        Member member = memberRepository.findByIdAndDeletedNot(memberDto.id()).orElseThrow(() -> new CommonException(ExceptionEnum.MEMBER_NOT_FOUND));

        checkNickname(memberDto.id(), memberDto.nickname());

        if(memberDto.name().getBytes().length < 4 || memberDto.name().getBytes().length > 30
                || memberDto.description().getBytes().length > 150) {
            throw new CommonException(ExceptionEnum.INVALID_MEMBER_DATA);
        }

        if (file != null && !file.isEmpty()){
            String previousOriginImg = member.getOriginImg();

            if(previousOriginImg != null && !previousOriginImg.isEmpty() && !previousOriginImg.equals(BASIC_PROFILE_IMAGE_NAME)) {
                s3Uploader.deleteFile(member.getOriginImg());
            }

            String originImg = s3Uploader.saveUploadFile(file);
            String imgUrl = s3Uploader.getFilePath(originImg);
            member.updateProfileImg(originImg, imgUrl);
        }

        member.updateProfileInfo(memberDto.name(), memberDto.nickname(), memberDto.description());
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDto.Response getMyProfile(Integer id) {
        Member member = memberRepository.findByIdAndDeletedNot(id).orElseThrow(() -> new CommonException(ExceptionEnum.MEMBER_NOT_FOUND));

        return MemberDto.Response.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .imgUrl(member.getImgUrl())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDto.Response getProfile(Integer id) {
        Member member = memberRepository.findByIdAndDeletedNot(id).orElseThrow(() -> new CommonException(ExceptionEnum.MEMBER_NOT_FOUND));

        return MemberDto.Response.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .imgUrl(member.getImgUrl())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDto.DetailResponse getDetailProfile(Integer id) {
        Member member = memberRepository.findByIdAndDeletedNot(id).orElseThrow(() -> new CommonException(ExceptionEnum.MEMBER_NOT_FOUND));

        return MemberDto.DetailResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .provider(member.getProvider())
                .description(member.getDescription())
                .imgUrl(member.getImgUrl())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public void checkEmail(String email) {
        if(memberRepository.findByEmail(email).isPresent()) {
            throw new CommonException(ExceptionEnum.ALREADY_EXIST_EMAIL);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void checkNickname(Integer id, String nickname) {
        if(nickname.getBytes().length < 4 ||nickname.getBytes().length > 30) {
            throw new CommonException(ExceptionEnum.INVALID_MEMBER_DATA);
        }

        for(String pn : PROHIBITED_NICKNAME) {
            if(nickname.equalsIgnoreCase(pn)) {
                throw new CommonException(ExceptionEnum.PROHIBITED_NICKNAME);
            }
        }

        Optional<Member> memberOptional = memberRepository.findByNickname(nickname);

        if(memberOptional.isPresent() && !memberOptional.orElseThrow().getId().equals(id)) {
            throw new CommonException(ExceptionEnum.ALREADY_EXIST_NICKNAME);
        }
    }

    @Override
    public void changePassword(MemberDto.PasswordPatch memberDto) {
        Member member = memberRepository.findByIdAndDeletedNot(memberDto.id()).orElseThrow(() -> new CommonException(ExceptionEnum.MEMBER_NOT_FOUND));

        if(!passwordEncoder.matches(memberDto.originPassword(), member.getPassword())) {
            throw new CommonException(ExceptionEnum.FAIL_AUTH_PASSWORD);
        }
        else if(memberDto.originPassword().equals(memberDto.updatePassword()) || !memberDto.updatePassword().equals(memberDto.updatePasswordCheck())) {
            throw new CommonException(ExceptionEnum.INVALID_MEMBER_DATA);
        }

        member.changePassword(passwordEncoder.encode(memberDto.updatePassword()));
    }

    @Override
    @Transactional(readOnly = true)
    public Member getMember(Integer memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
    }

    @Override
    public void sendAuthCode(String email) {
        checkEmail(email);

        String authCode = createAuthCode();

        emailService.sendEmail(email, authCode);
    }

    @Override
    public String createAuthCode() {
        try {
            StringBuilder builder = new StringBuilder();
            Random random = SecureRandom.getInstanceStrong();

            for (int i = 0; i < 6; i++) {
                builder.append(random.nextInt(10));
            }

            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new CommonException(ExceptionEnum.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void verifyAuthCode(String email, String authCode) {
        if(emailAuthRepository.findById(authCode + email).isEmpty()) {
            throw new CommonException(ExceptionEnum.NOT_MATCH_CODE);
        }

        emailAuthRepository.deleteById(authCode + email);
    }

    @Override
    public void withdrawalMember(HttpServletRequest request, HttpServletResponse response, Integer id) {
        Member member = memberRepository.findByIdAndDeletedNot(id).orElseThrow(() -> new CommonException(ExceptionEnum.MEMBER_NOT_FOUND));

        jwtUtil.destroyToken(request, response);

        member.deleteData();
    }
}