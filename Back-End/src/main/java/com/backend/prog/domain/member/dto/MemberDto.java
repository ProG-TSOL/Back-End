package com.backend.prog.domain.member.dto;

import com.backend.prog.domain.member.domain.Provider;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.util.List;

public class MemberDto {

    public record Post() {
    }

    public record ProfilePatch() {
    }

    public record PasswordPatch(@NotNull @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$") String originPassword
            , @NotNull @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$") String updatePassword
            , @NotNull @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$") String updatePasswordCheck) {
    }

    @Builder
    public record Response(Integer id, String nickName, String imgUrl) {
    }

    @Builder
    public record DetailResponse(Integer id, Provider provider, String name, String nickName, String description, String imgUrl, List<MemberTechDto.Response> memberTechList) {
    }

}
