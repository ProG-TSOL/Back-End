package com.backend.prog.domain.comment.dto;

import com.backend.prog.domain.member.dto.MemberDto;
import lombok.Builder;

import java.time.LocalDateTime;

public class CommentDto {
    public record Post(String contentCode, Integer memberId,
                       Long parentId, Long contentId, String content){
    }

    public record Patch(String content){

    }

    @Builder
    public record Response(Long id, MemberDto.Response member, boolean isParent, Long children, boolean isDeleted, String content, LocalDateTime createdAt, LocalDateTime modifiedAt){
    }
}
