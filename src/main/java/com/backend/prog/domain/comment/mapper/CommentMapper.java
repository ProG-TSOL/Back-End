package com.backend.prog.domain.comment.mapper;

import com.backend.prog.domain.comment.domain.Comment;
import com.backend.prog.domain.comment.dto.CommentDto;
import com.backend.prog.domain.comment.dto.CommentSimpleDto;
import com.backend.prog.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    public List<CommentDto.Response> dtoToResponses(List<CommentSimpleDto> list) {
        return list.stream().map(ct -> CommentDto.Response.builder()
                .id(ct.getId())
                .member(MemberDto.Response.builder()
                        .id(ct.getMember().getId())
                        .nickname(ct.getMember().getNickname())
                        .imgUrl(ct.getMember().getImgUrl())
                        .build()
                )
                .isDeleted(ct.isDeleted())
                .isParent(ct.getParentId() != null)
                .children(ct.getChildren())
                .content(ct.getContent())
                .createdAt(ct.getCreatedAt())
                .modifiedAt(ct.getModifiedAt())
                .build()
        ).toList();
    }

    public List<CommentDto.Response> entityToResponse(List<Comment> list) {
        return list.stream().map(ct -> CommentDto.Response.builder()
                .id(ct.getId())
                .member(MemberDto.Response.builder()
                        .id(ct.getMember().getId())
                        .nickname(ct.getMember().getNickname())
                        .imgUrl(ct.getMember().getImgUrl())
                        .build()
                )
                .isDeleted(ct.isDeleted())
                .isParent(ct.getParentId() != null)
                .content(ct.getContent())
                .createdAt(ct.getCreatedAt())
                .modifiedAt(ct.getModifiedAt())
                .build()
        ).toList();
    }
}
