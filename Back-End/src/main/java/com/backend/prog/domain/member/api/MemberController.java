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

import java.security.Principal;

@Log4j2
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberServiceImpl memberService;

    @GetMapping("/profile/{id}")
    public CommonApiResponse<?> getProfile(@PathVariable Integer id) {
        MemberDto.Response response = memberService.getProfile(id);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(response)
                .cnt(1)
                .build();
    }

    @GetMapping("/detail-profile/{id}")
    public CommonApiResponse<?> getDetailProfile(@PathVariable Integer id) {
        MemberDto.DetailResponse response = memberService.getDetailProfile(id);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(response)
                .cnt(1)
                .build();
    }

    @GetMapping("/validate-nickName/{nickName}")
    public CommonApiResponse<?> isUniqueNickName(@PathVariable String nickName) {
        boolean isPresent = !memberService.checkNickname(nickName);

        if (isPresent) {
            throw new CommonException(ExceptionEnum.ALREADY_EXIST_NICKNAME);
        }

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(nickName)
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
}