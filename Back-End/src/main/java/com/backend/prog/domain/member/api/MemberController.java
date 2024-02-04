package com.backend.prog.domain.member.api;

import com.backend.prog.domain.member.application.MemberServiceImpl;
import com.backend.prog.domain.member.dto.MemberDto;
import com.backend.prog.global.common.CommonApiResponse;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberServiceImpl memberService;

    @PostMapping("/sign-up")
    public CommonApiResponse<?> signUp(@RequestBody @Valid MemberDto.Post memberDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new CommonException(ExceptionEnum.INVALID_MEMBER_DATA);
        }

        memberService.signUp(memberDto);

       return  CommonApiResponse.builder()
               .status(HttpStatus.CREATED)
               .data(1)
               .cnt(1)
               .build();
    }

    @PatchMapping("/update-profile")
    public CommonApiResponse<?> updateProfile(Principal principal, @RequestPart("member") @Valid MemberDto.ProfilePatch memberDto, BindingResult bindingResult, @RequestPart(value = "file", required = false) MultipartFile file) {
        if(bindingResult.hasErrors()) {
            throw new CommonException(ExceptionEnum.INVALID_MEMBER_DATA);
        }

        memberService.updateProfile(Integer.parseInt(principal.getName()), memberDto, file);

        return  CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(1)
                .cnt(1)
                .build();
    }

    @GetMapping("/my-profile")
    public CommonApiResponse<?> getMyProfile(Principal principal) {
        MemberDto.Response response = memberService.getMyProfile(Integer.valueOf(principal.getName()));

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(response)
                .cnt(1)
                .build();
    }

    @GetMapping("/profile/{email}")
    public CommonApiResponse<?> getProfile(@PathVariable String email) {
        MemberDto.Response response = memberService.getProfile(email);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(response)
                .cnt(1)
                .build();
    }

    @GetMapping("/detail-profile/{email}")
    public CommonApiResponse<?> getDetailProfile(@PathVariable String email) {
        MemberDto.DetailResponse response = memberService.getDetailProfile(email);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(response)
                .cnt(1)
                .build();
    }

    @GetMapping("/nickname-validation-check/{nickname}")
    public CommonApiResponse<?> isUniqueNickName(@PathVariable String nickname) {
        memberService.checkNickname(0, nickname);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(nickname)
                .cnt(1)
                .build();
    }

    @PatchMapping("/change-password")
    public CommonApiResponse<?> changePassword(@RequestBody @Valid MemberDto.PasswordPatch memberDto, BindingResult bindingResult, Principal principal) {
        if(bindingResult.hasErrors()) {
            throw new CommonException(ExceptionEnum.INVALID_MEMBER_DATA);
        }

        memberService.changePassword(Integer.valueOf(principal.getName()), memberDto);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(null)
                .cnt(1)
                .build();
    }

    @PostMapping("/email-verification")
    public CommonApiResponse<?> sendAuthCode(@RequestBody Map<String, String> emailMap) {
        memberService.sendAuthCode(emailMap.get("email"));

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(null)
                .cnt(1)
                .build();
    }

    @PostMapping("/email-verification-confirm")
    public CommonApiResponse<?> verifyAuthCode(@RequestBody Map<String, String> map) {
        memberService.verifyAuthCode(map.get("email"), map.get("authCode"));

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(null)
                .cnt(1)
                .build();
    }
}