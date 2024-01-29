package com.backend.prog.domain.board.application;

import com.backend.prog.domain.board.dto.BoardDetailResponse;
import com.backend.prog.domain.board.dto.BoardListReponse;
import com.backend.prog.domain.board.dto.BoardSaveRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {

    // TODO : 게시글 등록, 수정, 삭제, 목록조회, 상세조회

    void saveBoard(BoardSaveRequest boardInfos, List<MultipartFile> files);
    void modifyBoard();
    void removeBoard();
    List<BoardListReponse> getBoardList();
    BoardDetailResponse getBoardDetail();

}
