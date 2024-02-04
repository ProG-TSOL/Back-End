package com.backend.prog.domain.member.dto;

import com.backend.prog.domain.member.domain.Provider;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.util.List;

public class MemberDto {

    public record Post(@Email @NotNull String email
            , @NotNull @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$")String password
            , @NotNull @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$")String passwordCheck
            , @NotNull @Pattern(regexp = "^([A-Za-z가-힣])+") String name
            , @NotNull @Pattern(regexp = "^([A-Za-z0-9ㄱ-ㅎㅏ-ㅣ가-힣])+") String nickname
            , Provider provider) {
    }

    public record ProfilePatch(@NotNull @Pattern(regexp = "^([A-Za-z가-힣])+") String name
            , @NotNull @Pattern(regexp = "^([A-Za-z0-9ㄱ-ㅎㅏ-ㅣ가-힣])+") String nickname
            , String description
            , List<MemberTechDto.Request> memberTechDtoList) {
    }

    public record PasswordPatch(@NotNull @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$") String originPassword
            , @NotNull @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$") String updatePassword
            , @NotNull @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$") String updatePasswordCheck) {
    }

    @Builder
    public record Response(Integer id, String nickname, String imgUrl) {
    }

    @Builder
    public record DetailResponse(Integer id, String email, Provider provider, String name, String nickname, String description, String imgUrl, List<MemberTechDto.Response> memberTechList) {
    }

}