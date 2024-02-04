package com.backend.prog.domain.board.dao.custom;

import com.backend.prog.domain.board.domain.Board;
import com.backend.prog.domain.project.domain.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BoardRepositoryCustom {

    Slice<Board> searchBoardList(Project project, Pageable pageable, Long lastBoardId);
}
