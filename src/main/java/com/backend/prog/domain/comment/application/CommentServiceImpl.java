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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
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
        Member findMember = getFindMember(post.memberId());

        //컨텐츠 타입 검증
        CodeDetail findCodeDetail = codeDetailRepository.findByDetailName(post.contentCode());

        Long contentId;

        //존재하는 컨텐츠인지 검증
        switch (findCodeDetail.getDetailDescription()) {
            case "프로젝트" -> {
                Project findProject = projectRespository.findById(post.contentId()).orElseThrow(() -> new CommonException(ExceptionEnum.INVALID_CONTENT_DATA));
                contentId = findProject.getId();
            }
            case "게시물" -> {
                Board findBoard = boardRepository.findById(post.contentId()).orElseThrow(() -> new CommonException(ExceptionEnum.INVALID_CONTENT_DATA));
                contentId = findBoard.getId();
                // 프로젝트에 참가한 멤버인지 거증
                getProjectMember(findBoard.getProject(), findMember.getId());
            }
            case "업무" -> {
                Work findWork = workRepository.findById(post.contentId()).orElseThrow(() -> new CommonException(ExceptionEnum.INVALID_CONTENT_DATA));
                contentId = findWork.getId();
                // 프로젝트에 참가한 멤버인지 거증
                getProjectMember(findWork.getProject(), findMember.getId());
            }
            default -> throw new CommonException(ExceptionEnum.INVALID_CONTENT_DATA);
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

    private ProjectMember getProjectMember(Project findWork, Integer findMember) {
        return projectMemberRespository.findById(new ProjectMemberId(findWork.getId(), findMember)).orElseThrow(() -> new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE));
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
            getProjectMember(findBoard.getProject(), memberId);
            List<CommentSimpleDto> list = commentRepository.getComments(findCodeDetail, contentId);
            return commentMapper.dtoToResponses(list);
        } else {
            Work findWork = workRepository.findById(contentId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

            //프로젝트 참여중인지 검증
            getProjectMember(findWork.getProject(), memberId);
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
        Member findMember = getFindMember(memberId);
        Comment findComment = getFindComment(commentId);

        if (!findComment.getMember().equals(findMember)) {
            throw new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE);
        }
        findComment.updateContent(patch.content());

        return CommentDto.Response.builder()
                .id(findComment.getId())
                .member(MemberDto.Response.builder()
                        .id(findComment.getMember().getId())
                        .nickname(findComment.getMember().getNickname())
                        .imgUrl(findComment.getMember().getImgUrl())
                        .build()
                )
                .isDeleted(findComment.isDeleted())
                .isParent(findComment.getParentId() != null)
                .content(findComment.getContent())
                .build();
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Integer memberId) {
        Member findMember = getFindMember(memberId);
        Comment findComment = getFindComment(commentId);

        if (!findComment.getMember().equals(findMember)) {
            throw new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE);
        }
        findComment.deleteData();
    }

    private Comment getFindComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
    }

    private Member getFindMember(Integer memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new CommonException(ExceptionEnum.INVALID_MEMBER_DATA));
    }
}
