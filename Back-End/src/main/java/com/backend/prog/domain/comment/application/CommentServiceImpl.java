package com.backend.prog.domain.comment.application;

import com.backend.prog.domain.board.dao.BoardRepository;
import com.backend.prog.domain.board.domain.Board;
import com.backend.prog.domain.comment.dao.CommentRepository;
import com.backend.prog.domain.comment.domain.Comment;
import com.backend.prog.domain.comment.dto.CommentDto;
import com.backend.prog.domain.comment.dto.CommentSimpleDto;
import com.backend.prog.domain.comment.mapper.CommentMapper;
import com.backend.prog.domain.manager.dao.CodeDetailRepository;
import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.member.dao.MemberRepository;
import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.domain.member.dto.MemberDto;
import com.backend.prog.domain.project.dao.ProjectMemberRespository;
import com.backend.prog.domain.project.dao.ProjectRespository;
import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.domain.project.domain.ProjectMember;
import com.backend.prog.domain.project.domain.ProjectMemberId;
import com.backend.prog.domain.work.dao.WorkRepository;
import com.backend.prog.domain.work.domain.Work;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ProjectRespository projectRespository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final WorkRepository workRepository;
    private final CodeDetailRepository codeDetailRepository;
    private final ProjectMemberRespository projectMemberRespository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public void createComment(CommentDto.Post post) {

        // 멤버 검증
        Member findMember = memberRepository.findById(post.memberId()).orElseThrow(() -> new CommonException(ExceptionEnum.INVALID_MEMBER_DATA));

        //컨텐츠 타입 검증
        CodeDetail findCodeDetail = codeDetailRepository.findByDetailName(post.contentCode());

        Long contentId = null;

        //존재하는 컨텐츠인지 검증
        if (findCodeDetail.getDetailDescription().equals("프로젝트")) {
            log.info("ok");
            Project findProject = projectRespository.findById(post.contentId()).orElseThrow(() -> new CommonException(ExceptionEnum.INVALID_CONTENT_DATA));
            contentId = findProject.getId();
        } else if (findCodeDetail.getDetailDescription().equals("게시물")) {
            Board findBoard = boardRepository.findById(post.contentId()).orElseThrow(() -> new CommonException(ExceptionEnum.INVALID_CONTENT_DATA));
            contentId = findBoard.getId();
            // 프로젝트에 참가한 멤버인지 거증
            ProjectMember projectMember = projectMemberRespository.findById(new ProjectMemberId(findBoard.getProject().getId(), findMember.getId())).orElseThrow(() -> new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE));
        } else if (findCodeDetail.getDetailDescription().equals("업무")) {
            Work findWork = workRepository.findById(post.contentId()).orElseThrow(() -> new CommonException(ExceptionEnum.INVALID_CONTENT_DATA));
            contentId = findWork.getId();
            // 프로젝트에 참가한 멤버인지 거증
            ProjectMember projectMember = projectMemberRespository.findById(new ProjectMemberId(findWork.getProject().getId(), findMember.getId())).orElseThrow(() -> new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE));
        } else {
            throw new CommonException(ExceptionEnum.INVALID_CONTENT_DATA);
        }

        Comment comment = Comment.builder()
                .contentCode(findCodeDetail)
                .member(findMember)
                .parentId(post.parentId())
                .contentId(contentId)
                .content(post.content())
                .build();

        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public List<CommentDto.Response> getParentComments(Integer memberId, String contentCode, Long contentId) {

        //컨텐츠 타입 검증
        CodeDetail findCodeDetail = codeDetailRepository.findByDetailName(contentCode);

        if (contentCode.equals("프로젝트")) {

            List<CommentSimpleDto> list = commentRepository.getComments(findCodeDetail, contentId);

            return commentMapper.dtoToResponses(list);
        } else if (contentCode.equals("게시물")) {
            Board findBoard = boardRepository.findById(contentId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

            //프로젝트 참여중인지 검증
            ProjectMember findProejctMember = projectMemberRespository.findById(
                    new ProjectMemberId(findBoard.getProject().getId(), memberId))
                    .orElseThrow(() -> new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE));

            List<CommentSimpleDto> list = commentRepository.getComments(findCodeDetail, contentId);

            return commentMapper.dtoToResponses(list);
        } else {

            Work findWork = workRepository.findById(contentId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

            //프로젝트 참여중인지 검증
            ProjectMember findProejctMember = projectMemberRespository.findById(new ProjectMemberId(findWork.getProject().getId(), memberId)).orElseThrow(() ->
                    new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE));

            List<CommentSimpleDto> list = commentRepository.getComments(findCodeDetail, contentId);

            return commentMapper.dtoToResponses(list);
        }
    }

    @Override
    public List<CommentDto.Response> getChildrenComments(Long parentId) {
        List<Comment> list = commentRepository.findByParentId(parentId);

        return commentMapper.entityToResponse(list);
    }

    @Override
    @Transactional
    public CommentDto.Response updateComment(Long commentId, Integer memberId, CommentDto.Patch patch) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new CommonException(ExceptionEnum.INVALID_MEMBER_DATA));

        Comment findComment = commentRepository.findById(commentId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        if (findComment.getMember().equals(findMember)) {
            findComment.updateContent(patch.content());
        } else {
            throw new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE);
        }

        return CommentDto.Response.builder()
                .id(findComment.getId())
                .member(MemberDto.Response.builder()
                        .id(findComment.getMember().getId())
                        .nickname(findComment.getMember().getNickname())
                        .imgUrl(findComment.getMember().getImgUrl())
                        .build()
                )
                .isDeleted(findComment.isDeleted())
                .isParent(findComment.getParentId() != null ? true : false)
                .content(findComment.getContent())
                .build();
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Integer memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new CommonException(ExceptionEnum.INVALID_MEMBER_DATA));

        Comment findComment = commentRepository.findById(commentId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        if (findComment.getMember().equals(findMember)) {
            findComment.deleteData();
        } else {
            throw new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE);
        }
    }
}
