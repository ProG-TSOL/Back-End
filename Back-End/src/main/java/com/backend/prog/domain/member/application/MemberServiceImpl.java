package com.backend.prog.domain.member.application;

import com.backend.prog.domain.manager.dao.CodeDetailRepository;
import com.backend.prog.domain.member.dao.MemberRepository;
import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.member.dao.MemberTechRepository;
import com.backend.prog.domain.member.domain.MemberTech;
import com.backend.prog.domain.member.domain.MemberTechId;
import com.backend.prog.domain.member.domain.Role;
import com.backend.prog.domain.member.dto.MemberDto;
import com.backend.prog.global.S3.S3Uploader;
import com.backend.prog.global.auth.api.OAuth2Api;
import com.backend.prog.global.auth.dao.EmailAuthRepository;
import com.backend.prog.global.auth.service.EmailService;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import com.backend.prog.domain.member.dto.MemberTechDto;
import com.backend.prog.global.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Base64;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final ObjectMapper objectMapper;

    private final JwtUtil jwtUtil;

    private final S3Uploader s3Uploader;

    private final OAuth2Api oAuth2Api;

    private final Environment environment;

    private final EmailService emailService;

    private final MemberRepository memberRepository;

    private final CodeDetailRepository codeDetailRepository;

    private final MemberTechRepository memberTechRepository;

    private final EmailAuthRepository emailAuthRepository;

    private final PasswordEncoder passwordEncoder;

    private final String[] PROHIBITED_NICKNAME = {"null", "undefined"};

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

        List<MemberTech> memberTechList = member.getTechs();
        List<MemberTech> newMemberTechList = new ArrayList<>();

        loop: for(MemberTechDto.Request memberTechDto : memberDto.memberTechDtoList()) {
            CodeDetail codeDetail = codeDetailRepository.findById(memberTechDto.techCode()).orElseThrow(() -> new CommonException(ExceptionEnum.INVALID_MEMBER_DATA));

            if(codeDetail.getCode().getId() != 4) {
                throw new CommonException(ExceptionEnum.INVALID_MEMBER_DATA);
            }

            for(MemberTech memberTech : memberTechList) {
                if(memberTechDto.techCode().equals(memberTech.getId().getTechCode())) {
                    memberTechList.remove(memberTech);

                    newMemberTechList.add(memberTech);

                    continue loop;
                }
            }

            newMemberTechList.add(memberTechRepository.save(MemberTech.builder()
                    .id(new MemberTechId(member.getId(), codeDetail.getId()))
                    .member(member)
                    .techCode(codeDetail)
                    .build()));
        }

        for(MemberTech memberTech : memberTechList) {
            memberTechRepository.delete(memberTech);
        }

        member.changeTechs(newMemberTechList);

        String originImg = null;
        String imgUrl = null;

        if (file != null && !file.isEmpty()){
            String previousOriginImg = member.getOriginImg();

            if(previousOriginImg != null && !previousOriginImg.isEmpty()) {
                s3Uploader.deleteFile(member.getOriginImg());
            }

            originImg = s3Uploader.saveUploadFile(file);
            imgUrl = s3Uploader.getFilePath(originImg);
            member.updateProfileWithImage(memberDto.name(), memberDto.nickname(), memberDto.description(), originImg, imgUrl);
        }
        else {
            member.updateProfile(memberDto.name(), memberDto.nickname(), memberDto.description());
        }
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
    public MemberDto.DetailResponse getDetailProfile(Integer id) {
        Member member = memberRepository.findByIdAndDeletedNot(id).orElseThrow(() -> new CommonException(ExceptionEnum.MEMBER_NOT_FOUND));
        List<MemberTech> memberTechList = member.getTechs();
        List<MemberTechDto.Response> memberTechDtoResponseList = new ArrayList<>();

        for(MemberTech memberTech : memberTechList) {
            CodeDetail codeDetail = memberTech.getTechCode();

            memberTechDtoResponseList.add(
                    MemberTechDto.Response.builder()
                            .id(codeDetail.getId())
                            .name(codeDetail.getDetailName())
                            .description(codeDetail.getDetailDescription())
                            .build()
            );
        }

        String gitUsername = member.getGitUsername();
        String readMe = null;

        if(member.getGitUsername() != null) {
            log.info(member.getGitUsername());
            try {
                URI uri = new URI(environment.getProperty("oauth2.github.base-url"));
                Object readMeObject = oAuth2Api.getReadMe(uri, gitUsername, gitUsername);

                Map<Object, String> result = objectMapper.convertValue(readMeObject, Map.class);

                readMe = new String(Base64.decodeBase64(result.get("content")));
            } catch (Exception e) {
                log.info(e);
            }
        }


        return MemberDto.DetailResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .provider(member.getProvider())
                .description(member.getDescription())
                .imgUrl(member.getImgUrl())
                .memberTechList(memberTechDtoResponseList)
                .readMe(readMe)
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