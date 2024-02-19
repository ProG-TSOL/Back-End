package com.backend.prog.domain.comment.dao;

import com.backend.prog.domain.comment.dto.CommentSimpleDto;
import com.backend.prog.domain.manager.domain.CodeDetail;

import java.util.List;

public interface CommentRepositoryCustom {
    List<CommentSimpleDto> getComments(CodeDetail findCodeDetail, Long contentId);
}
