package com.backend.prog.domain.member.dto;

import lombok.Builder;

public class MemberTechDto {

    public record Request(Integer techCode){
    }

    @Builder
    public record Response(Integer id, String name, String description) {
    }
}
