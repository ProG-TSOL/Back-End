package com.backend.prog.domain.board.api;

import com.backend.prog.domain.board.application.BoardService;
import com.backend.prog.domain.board.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public void saveBoard(@RequestPart("board") BoardSaveRequest boardInfos, @RequestPart(value = "imgs", required = false) List<MultipartFile> files) {
        boardService.saveBoard(boardInfos, files);
    }

    @GetMapping("/notice/{projectId}")
    public BoardListReponse getNoticeBoard(@PathVariable Long projectId) {
        return boardService.getNoticeBoard(projectId);
    }

    @GetMapping("/{projectId}")
    public BoardSliceResponse getBoardList(@PathVariable Long projectId, @RequestParam(required = false) Long lastBoardId) {
        return boardService.getBoardList(projectId, lastBoardId);
    }

    @GetMapping("/detail/{boardId}")
    public BoardDetailResponse getBoardDetail(@PathVariable Long boardId) {
        return boardService.getBoardDetail(boardId);
    }

    @PatchMapping("/{boardId}")
    public void removeBoard(@PathVariable Long boardId) {
        boardService.removeBoard(boardId);
    }

    @PatchMapping("/detail/{boardId}")
    public void modify(@PathVariable Long boardId, @RequestPart("board") BoardModifyRequest request
            , @RequestPart(value = "imgs", required = false) List<MultipartFile> files) {
        boardService.modifyBoard(boardId, request, files);
    }

    // 공지체크
    @PatchMapping("/{projectId}/{boardId}")
    public void isCheckNotice(@PathVariable Long projectId, @PathVariable Long boardId) {
        boardService.isCheckNotice(projectId, boardId);
    }

}
