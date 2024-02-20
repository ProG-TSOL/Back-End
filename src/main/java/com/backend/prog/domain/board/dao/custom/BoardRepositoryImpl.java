package com.backend.prog.domain.board.dao.custom;

import com.backend.prog.domain.board.domain.Board;
import com.backend.prog.domain.project.domain.Project;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.backend.prog.domain.board.domain.QBoard.board;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<Board> searchBoardList(Project project, Pageable pageable, Long lastBoardId) {
        List<Board> content = jpaQueryFactory.selectFrom(board)
                .where(board.project.eq(project),
                        board.isDeleted.eq(false),
                        ltBoardId(lastBoardId)
                )
                .orderBy(board.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }
            return new SliceImpl<>(content, pageable, hasNext);
    }

    private BooleanExpression ltBoardId(Long boardId) {
        if (boardId == null) {
            return null;
        }
        return board.id.lt(boardId);
    }
}
