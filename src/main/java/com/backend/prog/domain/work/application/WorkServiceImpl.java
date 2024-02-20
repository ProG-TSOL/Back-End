package com.backend.prog.domain.work.application;

import com.backend.prog.domain.comment.dao.CommentRepository;
import com.backend.prog.domain.comment.domain.Comment;
import com.backend.prog.domain.comment.dto.CommentDto;
import com.backend.prog.domain.comment.dto.CommentSimpleDto;
import com.backend.prog.domain.comment.mapper.CommentMapper;
import com.backend.prog.domain.feed.application.FeedServiceimpl;
import com.backend.prog.domain.manager.application.CodeCommonService;
import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.manager.dto.CodeDetailSimpleResponse;
import com.backend.prog.domain.member.dao.MemberRepository;
import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.domain.project.dao.ProjectRespository;
import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.domain.work.dao.WorkCheckListRepository;
import com.backend.prog.domain.work.dao.WorkRepository;
import com.backend.prog.domain.work.domain.Work;
import com.backend.prog.domain.work.domain.WorkCheckList;
import com.backend.prog.domain.work.dto.*;
import com.backend.prog.global.common.CheckDataExist;
import com.backend.prog.global.common.DeleteEntity;
import com.backend.prog.global.common.constant.CommonCode;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkServiceImpl implements WorkService {
    private final WorkRepository workRepository;
    private final WorkCheckListRepository workCheckListRepository;

    private final CodeCommonService codeCommonService;

    private final ProjectRespository projectRespository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    private final FeedServiceimpl feedServiceimpl;
    private final CommentMapper commentMapper;

    @Override
    public void saveWork(CreateWorkRequest workRequest) {
        // 프로젝트 ID
        Project project = projectRespository.findById(workRequest.projectId())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        // 신청자 ID
        Member producer = memberRepository.findById(workRequest.producerId())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        // 업무상태코드
        CodeDetail statusCode = codeCommonService.getCodeDetail(workRequest.statusCode());
        // 업무구분코드
        CodeDetail typeCode = codeCommonService.getCodeDetail(workRequest.typeCode());
        // 우선순위코드
        CodeDetail priorityCode = codeCommonService.getCodeDetail(workRequest.priorityCode());
        // 담당자 ID
        Member consumer = memberRepository.findById(workRequest.consumerId())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        // 1. 업무 저장
        Work entity = Work.builder()
                .project(project)
                .producerId(producer)
                .statusCode(statusCode)
                .typeCode(typeCode)
                .priorityCode(priorityCode)
                .consumerId(consumer)
                .title(workRequest.title())
                .content(workRequest.content())
                .startDay(workRequest.startDay())
                .endDay(workRequest.endDay())
                .build();

        workRepository.save(entity);

        // 2. 피드 생성
        Map<String, Object> feedDtoMap = Map.of(
                "projectId", project.getId(),
                "contentsId", entity.getId(),
                "memberId", entity.getProducerId().getId()
        );
        feedServiceimpl.makeFeedDto(CommonCode.CONTENT_WORK, feedDtoMap);
    }

    private static List<WorkListResponse> getWorkListResponses(List<Work> workList) {
        CheckDataExist.checkData(workList);

        return workList.stream()
                .map(entity -> WorkListResponse.builder()
                        .workId(entity.getId())
                        .statusCode(new CodeDetailSimpleResponse().toDto(entity.getStatusCode()))
                        .typeCode(new CodeDetailSimpleResponse().toDto(entity.getTypeCode()))
                        .priorityCode(new CodeDetailSimpleResponse().toDto(entity.getPriorityCode()))
                        .producerMemberName(entity.getProducerId().getName())
                        .title(entity.getTitle())
                        .startDay(entity.getStartDay())
                        .endDay(entity.getEndDay())
                        .build())
                .toList();
    }

    @Override
    public List<WorkListResponse> getWorkList(Long projectId) {
        Project project = projectRespository.findById(projectId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        List<Work> workList = workRepository.findAllByProject(project);
        return getWorkListResponses(workList);
    }

    @Override
    public List<WorkListResponse> getWorkListSearchByKeyword(Long projectId, String title) {
        Project project = projectRespository.findById(projectId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        List<Work> workList = workRepository.findAllByTitle(project, title);
        return getWorkListResponses(workList);
    }

    @Override
    public WorkDetailResponse getWorkDetail(Long workId) {
        // 필요정보 : 멤버, 업무, 업무체크리스트, 댓글
        // 1. 업무
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        // 3. 업무 체크리스트
        List<WorkCheckList> workCheckLists = workCheckListRepository.findAllByWork(work);
        List<CheckListResponse> checkList = workCheckLists.stream()
                .map(entity -> new CheckListResponse().toDto(entity))
                .toList();

        // 4. 댓글 상세코드:업무, 컨텐츠ID:업무ID
        CodeDetail codeDetail = codeCommonService.getCodeDetailByNames(CommonCode.CODE_CONTENT, CommonCode.CONTENT_WORK);

        List<CommentSimpleDto> list = commentRepository.getComments(codeDetail, workId);
        List<CommentDto.Response> comments = commentMapper.dtoToResponses(list);

        return WorkDetailResponse.builder()
                .workId(work.getId())
                .createdAt(work.getCreatedAt())
                .workStatusCode(new CodeDetailSimpleResponse().toDto(work.getStatusCode()))
                .workTitle(work.getTitle())
                .workContent(work.getContent())
                .startDay(work.getStartDay())
                .endDay(work.getEndDay())
                .checkList(checkList)
                .comments(comments)
                .build();
    }

    @Override
    public void modifyWork(Long workId, UpdateWorkRequest workRequest) {
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        CodeDetail statusCode = codeCommonService.getCodeDetail(workRequest.statusCode());
        CodeDetail typeCode = codeCommonService.getCodeDetail(workRequest.typeCode());
        CodeDetail priorityCode = codeCommonService.getCodeDetail(workRequest.priorityCode());

        Member consumer = memberRepository.findById(workRequest.consumerId())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        work.updateWork(statusCode, typeCode, priorityCode, consumer,
                workRequest.title(), workRequest.content(), workRequest.startDay(), workRequest.endDay());
    }

    @Override
    public void modifyWorkStatus(Long workId, Integer statusCode) {
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        CodeDetail status = codeCommonService.getCodeDetail(statusCode);

        work.updateWorkStatus(status);
    }

    @Override
    @Transactional
    public void removeWork(Long workId) {
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        List<WorkCheckList> workCheckLists = workCheckListRepository.findAllByWork(work);

        // 1. 업무체크리스트 삭제
        if (!workCheckLists.isEmpty()) {
            workCheckListRepository.deleteAll(workCheckLists);
        }

        // 2. 댓글 삭제처리 (isdelete -> true 업데이트) contentCode:업무, contentId:업무ID
        CodeDetail codeDetail = codeCommonService.getCodeDetailByNames(CommonCode.CODE_CONTENT, CommonCode.CONTENT_WORK);
        List<Comment> commentList = commentRepository.inquriyForDeletion(codeDetail, workId);
        if (!commentList.isEmpty()) {
            commentList.forEach(DeleteEntity::deleteData);
        }

        // 3. 업무 삭제
        workRepository.delete(work);

    }

}