package com.backend.prog.domain.project.dto;

import lombok.Builder;

public class AdditionalDto {
    public record Post(String title, String url){

    }

    public record Patch(String title, String url){

    }

    @Builder
    public record Response(Long id, String title, String url, String imgUrl){
    }
}
