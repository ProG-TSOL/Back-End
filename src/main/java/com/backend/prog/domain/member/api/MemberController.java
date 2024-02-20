package com.backend.prog.domain.member.api;

import com.backend.prog.domain.member.application.MemberService;
import com.backend.prog.domain.member.dto.MemberDto;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public void signUp(@RequestBody @Valid MemberDto.Post memberDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new CommonException(ExceptionEnum.INVALID_MEMBER_DATA);
        }

        memberService.signUp(memberDto);
    }

    @PreAuthorize("#memberDto.id.equals(principal)")
    @PatchMapping("/update-profile")
    public void updateProfile(@RequestPart("member") @Valid MemberDto.ProfilePatch memberDto, BindingResult bindingResult, @RequestPart(value = "file", required = false) MultipartFile file) {
        if(bindingResult.hasErrors()) {
            throw new CommonException(ExceptionEnum.INVALID_MEMBER_DATA);
        }

        memberService.updateProfile(memberDto, file);
    }

    @GetMapping("/my-profile")
    public Object getMyProfile(Principal principal) {
        MemberDto.Response response = memberService.getMyProfile(Integer.valueOf(principal.getName()));

        return response;
    }

    @GetMapping("/profile/{id}")
    public Object getProfile(@PathVariable Integer id) {
        MemberDto.Response response = memberService.getProfile(id);

        return response;
    }

    @GetMapping("/detail-profile/{id}")
    public Object getDetailProfile(@PathVariable Integer id) {
        MemberDto.DetailResponse response = memberService.getDetailProfile(id);

        return response;
    }

    @PostMapping(value = {"/nickname-validation-check/{id}", "/nickname-validation-check"})
    public void isUniqueNickName(@PathVariable(required = false) Integer id ,@RequestBody @Valid MemberDto.nicknamePost memberDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new CommonException(ExceptionEnum.INVALID_MEMBER_DATA);
        }

        memberService.checkNickname(id, memberDto.nickname());
    }

    @PreAuthorize("#memberDto.id.equals(principal)")
    @PatchMapping("/change-password")
    public void changePassword(@RequestBody @Valid MemberDto.PasswordPatch memberDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new CommonException(ExceptionEnum.INVALID_MEMBER_DATA);
        }

        memberService.changePassword(memberDto);
    }

    @PostMapping("/email-verification")
    public void sendAuthCode(@RequestBody @Valid MemberDto.emailPost memberDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CommonException(ExceptionEnum.INVALID_MEMBER_DATA);
        }

        memberService.sendAuthCode(memberDto.email());
    }

    @PostMapping("/email-verification-confirm")
    public void verifyAuthCode(@RequestBody MemberDto.emailVerificationPost memberDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new CommonException(ExceptionEnum.INVALID_MEMBER_DATA);
        }

        memberService.verifyAuthCode(memberDto.email(), memberDto.authCode());
    }

    @PreAuthorize("#id.equals(principal)")
    @DeleteMapping("/withdrawal-member/{id}")
    public void withdrawalMember(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
        memberService.withdrawalMember(request, response, id);

        SecurityContextHolder.clearContext();
    }
}