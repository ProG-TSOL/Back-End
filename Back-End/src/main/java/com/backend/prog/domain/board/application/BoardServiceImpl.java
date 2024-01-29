package com.backend.prog.domain.board.application;

import com.backend.prog.domain.board.dao.BoardImgRepository;
import com.backend.prog.domain.board.dao.BoardRepository;
import com.backend.prog.domain.board.domain.Board;
import com.backend.prog.domain.board.domain.BoardImage;
import com.backend.prog.domain.board.dto.BoardDetailResponse;
import com.backend.prog.domain.board.dto.BoardListReponse;
import com.backend.prog.domain.board.dto.BoardSaveRequest;
import com.backend.prog.domain.member.dao.MemberRepository;
import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.domain.project.dao.ProjectRespository;
import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.global.S3.S3Uploader;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Log4j2
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final BoardImgRepository boardImgRepository;
    private final ProjectRespository projectRespository;
    private final MemberRepository memberRepository;

    private final ModelMapper mapper;
    private final S3Uploader s3Uploader;


    @Override
    @Transactional
    public void saveBoard(BoardSaveRequest boardInfos, List<MultipartFile> files) {
        // 1. 게시글 저장
        Project project = projectRespository.findById(boardInfos.projectId())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        Member member = memberRepository.findById(boardInfos.memberId())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        Board entity = Board.builder()
                .title(boardInfos.title())
                .content(boardInfos.content())
                .isNotice(boardInfos.isNotice())
                .project(project)
                .member(member)
                .build();
        Board saveBoard = boardRepository.save(entity);
        // 2. 파일 저장
        List<String> fileUrls;
        if (files != null && !files.isEmpty()) {
            fileUrls = new ArrayList<>();

            files.forEach(file -> {
                String fileName = s3Uploader.saveUploadFile(file);
                // 벌크 insert를 위해 url list 저장
                String filePath = s3Uploader.getFilePath(fileName);
//                log.info("■■■■■ filePath : {} ■■■■■", filePath);
                fileUrls.add(filePath);
            });
        } else {
            fileUrls = null;
        }

        if (fileUrls != null) {
            // 3. 파일 저장 경로 저장
            List<BoardImage> imgList = fileUrls.stream()
                    .map(url -> BoardImage.builder()
                            .board(saveBoard)
                            .imgUrl(url)
                            .build())
                    .toList();

            boardImgRepository.saveAll(imgList);
        }

    }

    @Override
    public void modifyBoard() {

    }

    @Override
    public void removeBoard() {

    }

    @Override
    public List<BoardListReponse> getBoardList() {
        return null;
    }

    @Override
    public BoardDetailResponse getBoardDetail() {
        return null;
    }
}
