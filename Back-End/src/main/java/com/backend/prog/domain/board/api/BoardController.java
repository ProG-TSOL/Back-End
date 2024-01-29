package com.backend.prog.domain.board.api;

import com.backend.prog.domain.board.application.BoardService;
import com.backend.prog.domain.board.dto.BoardSaveRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
@Log4j2
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public void saveBoard(@RequestPart("board") BoardSaveRequest boardInfos, @RequestPart("imgs") List<MultipartFile> files) {
        boardService.saveBoard(boardInfos, files);
    }

}
