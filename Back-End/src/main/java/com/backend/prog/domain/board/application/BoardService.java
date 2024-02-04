package com.backend.prog.domain.board.application;

import com.backend.prog.domain.board.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {

    void saveBoard(BoardSaveRequest boardInfos, List<MultipartFile> files);
    void modifyBoard(Long boardId, BoardModifyRequest request, List<MultipartFile> files);
    void removeBoard(Long boardId);

    BoardListReponse getNoticeBoard(Long projectId);
    BoardSliceResponse getBoardList(Long projectId, Long lastBoardId);
    BoardDetailResponse getBoardDetail(Long boardId);

    void isCheckNotice(Long projectId, Long boardId);
}
