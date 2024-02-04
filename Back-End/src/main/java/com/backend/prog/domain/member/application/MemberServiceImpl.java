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
import com.backend.prog.global.auth.dao.EmailAuthRepository;
import com.backend.prog.global.auth.service.EmailService;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import com.backend.prog.domain.member.dto.MemberTechDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final S3Uploader s3Uploader;

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
    public void updateProfile(Integer id, MemberDto.ProfilePatch memberDto, MultipartFile file) {
        Member member = memberRepository.findByIdAndDeletedNot(id).orElseThrow(() -> new CommonException(ExceptionEnum.MEMBER_NOT_FOUND));

        checkNickname(id, memberDto.nickname());

        if(memberDto.name().getBytes().length < 4 || memberDto.name().getBytes().length > 30
                || memberDto.description().getBytes().length > 150) {
            throw new CommonException(ExceptionEnum.INVALID_MEMBER_DATA);
        }

        List<MemberTech> memberTechList = member.getTechs();
        List<MemberTech> newMemberTechList = new ArrayList<>();

        loop: for(MemberTechDto.Request memberTechDto : memberDto.memberTechDtoList()) {
            CodeDetail codeDetail = codeDetailRepository.findById(memberTechDto.techCode()).orElseThrow(() -> new CommonException(ExceptionEnum.INVALID_MEMBER_DATA));

            if(codeDetail.getCode().getId() != 4 || memberTechDto.techLevel() < 1 || memberTechDto.techLevel() > 5) {
                throw new CommonException(ExceptionEnum.INVALID_MEMBER_DATA);
            }

            for(MemberTech memberTech : memberTechList) {
                if(memberTechDto.techCode().equals(memberTech.getId().getTechCode())) {
                    if (!memberTechDto.techLevel().equals(memberTech.getTechLevel())) {
                        memberTech.updateTechLevel(memberTechDto.techLevel());
                    }

                    memberTechList.remove(memberTech);

                    newMemberTechList.add(memberTech);

                    continue loop;
                }
            }

            newMemberTechList.add(memberTechRepository.save(MemberTech.builder()
                    .id(new MemberTechId(id, codeDetail.getId()))
                    .member(member)
                    .techCode(codeDetail)
                    .techLevel(memberTechDto.techLevel())
                    .build()));
        }

        for(MemberTech memberTech : memberTechList) {
            memberTechRepository.delete(memberTech);
        }

        String originImg = null;
        String imgUrl = null;

        if (file != null && !file.isEmpty()){
            String previousOriginImg = member.getOriginImg();

            if(previousOriginImg != null && !previousOriginImg.isEmpty()) {
                s3Uploader.deleteFile(member.getOriginImg());
            }

            originImg = s3Uploader.saveUploadFile(file);
            imgUrl = s3Uploader.getFilePath(originImg);
        }

        member.changeTechs(newMemberTechList);
        member.updateProfile(memberDto.name(), memberDto.nickname(), memberDto.description(), originImg, imgUrl);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDto.Response getMyProfile(Integer id) {
        Member member = memberRepository.findByIdAndDeletedNot(id).orElseThrow(() -> new CommonException(ExceptionEnum.MEMBER_NOT_FOUND));

        return MemberDto.Response.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .imgUrl(member.getImgUrl())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDto.Response getProfile(String email) {
        Member member = memberRepository.findByEmailAndDeletedNot(email).orElseThrow(() -> new CommonException(ExceptionEnum.MEMBER_NOT_FOUND));

        return MemberDto.Response.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .imgUrl(member.getImgUrl())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDto.DetailResponse getDetailProfile(String email) {
        Member member = memberRepository.findByEmailAndDeletedNot(email).orElseThrow(() -> new CommonException(ExceptionEnum.MEMBER_NOT_FOUND));
        List<MemberTech> memberTechList = member.getTechs();
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
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .description(member.getDescription())
                .memberTechList(memberTechDtoResponseList)
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

        Optional<Member> member = memberRepository.findByNickname(nickname);

        if(member.isPresent() && !member.orElseThrow().getId().equals(id)) {
            throw new CommonException(ExceptionEnum.ALREADY_EXIST_NICKNAME);
        }
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
}