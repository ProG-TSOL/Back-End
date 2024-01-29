package com.backend.prog.domain.member.dto;

public class MemberTechDto {
    public record Response(String techName, String techImgUrl, Integer techLevel) {
    }
}
