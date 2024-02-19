package com.backend.prog.domain.board.dao;

import com.backend.prog.domain.board.dao.custom.BoardRepositoryCustom;
import com.backend.prog.domain.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
    @Query("select b from Board b where b.project.id = :projectId and b.isNotice = true")
    Board findByNotice(@Param("projectId") Long projectId);
}
