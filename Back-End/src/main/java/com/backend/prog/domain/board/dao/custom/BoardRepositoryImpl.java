package com.backend.prog.domain.board.dao.custom;

import com.backend.prog.domain.board.domain.Board;
import com.backend.prog.domain.board.domain.QBoard;
import com.backend.prog.domain.board.dto.BoardListReponse;
import com.backend.prog.domain.project.domain.Project;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.backend.prog.domain.board.domain.QBoard.board;

@RequiredArgsConstructor
@Log4j2
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final EntityManager em;
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

//    private SliceImpl<Board> test1_offset(Project project, Pageable pageable) {
//        log.debug("받은 offset: {} ",pageable.getOffset());
//        long start = System.currentTimeMillis();
//        List<Board> content = jpaQueryFactory.selectFrom(board)
//                .where(board.project.eq(project))
//                .orderBy(board.id.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize() + 1)
//                .fetch();
//
////        content.forEach(entity-> log.debug("■■■■■ 조회결과 board : {} ■■■■■", content));
//
//        boolean hasNext = false;
//        if (content.size() > pageable.getPageSize()) {
//            content.remove(pageable.getPageSize());
//            hasNext = true;
//        }
//
//        long end = System.currentTimeMillis();
//        log.debug("■■■■■ test1 소요시간 : {} 초■■■■■", (end - start));
////        log.debug("■■■■■ hasNext : {} ■■■■■", hasNext);
//
//        return new SliceImpl<>(content, pageable, hasNext);
//    }
}
