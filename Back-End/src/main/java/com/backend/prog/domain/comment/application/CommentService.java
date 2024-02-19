package com.backend.prog.domain.comment.application;

import com.backend.prog.domain.comment.dto.CommentDto;

import java.util.List;

public interface CommentService {
    void createComment(CommentDto.Post post);

    List<CommentDto.Response> getParentComments(Integer memberId, String contentCode, Long contentId);

    List<CommentDto.Response> getChildrenComments(Long parentId);

    CommentDto.Response updateComment(Long commentId, Integer memberId, CommentDto.Patch patch);

    void deleteComment(Long commentId, Integer memberId);
}
