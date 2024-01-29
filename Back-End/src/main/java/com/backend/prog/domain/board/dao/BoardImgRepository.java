package com.backend.prog.domain.board.dao;

import com.backend.prog.domain.board.domain.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardImgRepository extends JpaRepository<BoardImage, Long>{
}
