package com.backend.prog.domain.member.dto;

import com.backend.prog.domain.member.domain.Provider;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

public class MemberDto {

    public record Post(@NotNull @Email String email
            , @NotNull @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$")String password
            , @NotNull @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$")String passwordCheck
            , @NotNull @Pattern(regexp = "^([A-Za-z가-힣])+") String name
            , @NotNull @Pattern(regexp = "^([A-Za-z0-9ㄱ-ㅎㅏ-ㅣ가-힣])+") String nickname
            , Provider provider) {
    }

    public record ProfilePatch(Integer id
            , @NotNull @Pattern(regexp = "^([A-Za-z가-힣])+") String name
            , @NotNull @Pattern(regexp = "^([A-Za-z0-9ㄱ-ㅎㅏ-ㅣ가-힣])+") String nickname
            , String description) {
    }

    public record PasswordPatch(Integer id
            , @NotNull @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$") String originPassword
            , @NotNull @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$") String updatePassword
            , @NotNull @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$") String updatePasswordCheck) {
    }

    public record emailPost(@NotNull @Email String email) {
    }

    public record emailVerificationPost(@NotNull @Email String email, String authCode) {

    }

    public record nicknamePost(@NotNull @Pattern(regexp = "^([A-Za-z0-9ㄱ-ㅎㅏ-ㅣ가-힣])+") String nickname) {
    }

    @Builder
    public record Response(Integer id, String email, String nickname, String imgUrl) {
    }

    @Builder
    public record DetailResponse(Integer id, String email, Provider provider, String name, String nickname, String description, String imgUrl) {
    }

}