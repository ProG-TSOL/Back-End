package com.backend.prog.domain.member.api;

import com.backend.prog.domain.member.application.MemberServiceImpl;
import com.backend.prog.domain.member.dto.MemberDto;
import com.backend.prog.global.common.CommonApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/mini-profile/{id}")
    public CommonApiResponse<?> getMiniProfile(@PathVariable Integer id) {
        MemberDto.SimpleResponse response = memberService.getMiniProfile(id);

        return CommonApiResponse.builder()
                .status(HttpStatus.OK)
                .data(response)
                .cnt(1)
                .build();
    }
}