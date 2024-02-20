package com.backend.prog.domain.board.application;

import com.backend.prog.domain.board.dao.BoardImgRepository;
import com.backend.prog.domain.board.dao.BoardRepository;
import com.backend.prog.domain.board.domain.Board;
import com.backend.prog.domain.board.domain.BoardImage;
import com.backend.prog.domain.board.dto.*;
import com.backend.prog.domain.feed.application.FeedServiceimpl;
import com.backend.prog.domain.member.dao.MemberRepository;
import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.domain.project.dao.ProjectMemberRespository;
import com.backend.prog.domain.project.dao.ProjectRespository;
import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.domain.project.domain.ProjectMember;
import com.backend.prog.domain.project.domain.ProjectMemberId;
import com.backend.prog.global.S3.S3Uploader;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardImgRepository boardImgRepository;
    private final ProjectRespository projectRespository;
    private final MemberRepository memberRepository;
    private final ProjectMemberRespository projectMemberRespository;

    private final S3Uploader s3Uploader;
    private final FeedServiceimpl feedServiceimpl;

    private final int PAGE_SIZE = 15;

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

        saveFileImages(files, saveBoard);

        // 2. 피드 생성
        Map<String, Object> feedDtoMap = Map.of(
                "projectId", project.getId(),
                "contentsId", entity.getId(),
                "memberId", member.getId()
        );
        feedServiceimpl.makeFeedDto("Post", feedDtoMap);

    }

    /**
     * 이미지 파일 저장
     */
    @Transactional
    protected void saveFileImages(List<MultipartFile> files, Board saveBoard) {
        // 1. 파일 존재 체크
        if (files == null) {
            return;
        }
        boolean isEmpty = files.stream()
                .anyMatch(MultipartFile::isEmpty);
        if (isEmpty) {
            return;
        }

        // 2. 파일 저장
        List<String> fileUrls = new ArrayList<>();
                    files.forEach(file -> {
                String fileName = s3Uploader.saveUploadFile(file);
                // 벌크 insert를 위해 url list 저장
                String filePath = s3Uploader.getFilePath(fileName);
                fileUrls.add(filePath);
            });

        List<BoardImage> imgList = fileUrls.stream()
                .map(url -> BoardImage.builder()
                        .board(saveBoard)
                        .imgUrl(url)
                        .build())
                .toList();

        boardImgRepository.saveAll(imgList);
    }

    @Override
    @Transactional
    public void modifyBoard(Long boardId, BoardModifyRequest request, List<MultipartFile> files) {
        // 1.게시글 수정
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        board.update(request.title(), request.content(), request.isNotice());

        // 2.게시글 이미지 수정, 이미지가 있으면 삭제후 저장
        List<BoardImage> images = boardImgRepository.findAllByBoard(board);
        if (!images.isEmpty()) {
            boardImgRepository.deleteAll(images);
        }

        saveFileImages(files, board);
    }

    @Override
    @Transactional
    public void removeBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        board.deleteData();
    }

    @Override
    public BoardListReponse getNoticeBoard(Long projectId) {
        Board board = boardRepository.findByNotice(projectId);
        Member member = memberRepository.findById(board.getMember().getId())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        ProjectMember projectMember = projectMemberRespository.findById(new ProjectMemberId(board.getProject().getId(), member.getId()))
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        return new BoardListReponse().toDto(member, board, projectMember.getJobCode().getDetailDescription());
    }

    @Override
    public BoardSliceResponse getBoardList(Long projectId, Long lastBoardId) {
        PageRequest pageRequest = PageRequest.of(0, PAGE_SIZE);
        Project project = projectRespository.findById(projectId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        Slice<Board> result = boardRepository.searchBoardList(project, pageRequest, lastBoardId);
        List<Board> boardList = result.getContent();

        // 작성자 조회
        List<BoardListReponse> reponseList = boardList.stream()
                .map(board -> {
                    Member member = board.getMember();
                    Member writer = memberRepository.findById(member.getId())
                            .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

                    ProjectMember projectMember = projectMemberRespository.findById(new ProjectMemberId(board.getProject().getId(), member.getId()))
                            .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

                    return new BoardListReponse().toDto(writer, board, projectMember.getJobCode().getDetailDescription());
                })
                .toList();

        return new BoardSliceResponse().toDto(
                reponseList,
                result.hasNext(),
                !boardList.isEmpty() ? boardList.get(boardList.size() - 1).getId() : null
        );
    }

    @Override
    @Transactional
    public BoardDetailResponse getBoardDetail(Long boardId) {
        // 1.게시글
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        // 1.1 게시글 이미지
        List<BoardImage> images = boardImgRepository.findAllByBoard(board);
        board.addViewCnt();

        // 2.회원
        Member member = memberRepository.findById(board.getMember().getId())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        // 3.포지션
        ProjectMember projectMember = projectMemberRespository.findById(new ProjectMemberId(board.getProject().getId(), member.getId()))
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        return new BoardDetailResponse().toDto(member, board, images, projectMember.getJobCode().getDetailDescription());
    }

    @Override
    @Transactional
    public void isCheckNotice(Long projectId, Long boardId) {
        // 기존에 공지체크되어있던것은 해제
        Board board = boardRepository.findByNotice(projectId);
        if (board != null) {
            board.changeNotice();
        }
        // 공지 체크
        Board result = boardRepository.findById(boardId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        result.changeNotice();
    }
}