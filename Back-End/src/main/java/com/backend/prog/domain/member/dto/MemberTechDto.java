package com.backend.prog.domain.member.dto;

import lombok.Builder;

public class MemberTechDto {
    @Builder
    public record Response(Integer id, String name, String description, String techImgUrl, Integer techLevel) {
    }
}
