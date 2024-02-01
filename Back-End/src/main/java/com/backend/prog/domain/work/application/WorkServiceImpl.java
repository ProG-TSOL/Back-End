package com.backend.prog.domain.work.application;

import com.backend.prog.domain.manager.dao.CodeDetailRepository;
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
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class WorkServiceImpl implements WorkService {

    private final WorkRepository workRepository;
    private final WorkCheckListRepository workCheckListRepository;

    private final CodeDetailRepository codeDetailRepository;
    private final ModelMapper modelMapper;
    private final ProjectRespository projectRespository;
    private final MemberRepository memberRepository;

    @Override
    public void saveWork(CreateWorkRequest workRequest) {
        // 프로젝트 ID
        Project project = projectRespository.findById(workRequest.projectId())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        // 신청자 ID
        Member producer = memberRepository.findById(workRequest.producerId())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        // 업무상태코드
        CodeDetail statusCode = codeDetailRepository.findById(workRequest.statusCode())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        // 업무구분코드
        CodeDetail typeCode = codeDetailRepository.findById(workRequest.typeCode())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        // 우선순위코드
        CodeDetail priorityCode = codeDetailRepository.findById(workRequest.priorityCode())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        // 담당자 ID
        Member consumer = memberRepository.findById(workRequest.consumerId())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

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
    }

    @Override
    public List<WorkListResponse> getWorkList(Long projectId) {
        Project project = projectRespository.findById(projectId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        List<Work> workList = workRepository.findAllByProject(project);
        CheckDataExist.checkData(workList);

        return workList.stream()
                .map(entity -> WorkListResponse.builder()
                        .workId(entity.getId())
                        .statusCode(CodeDetailSimpleResponse.toDto(entity.getStatusCode()))
                        .typeCode(CodeDetailSimpleResponse.toDto(entity.getTypeCode()))
                        .priorityCode(CodeDetailSimpleResponse.toDto(entity.getPriorityCode()))
                        .producerMemberName(entity.getProducerId().getName())
                        .title(entity.getTitle())
                        .startDay(entity.getStartDay())
                        .endDay(entity.getEndDay())
                        .build())
                .toList();
    }

    // TODO : 검색 처리
    @Override
    public List<WorkListResponse> getWorkListSearchByKeyword(SearchWorkRequest request) {
        return null;
    }

    @Override
    public WorkDetailResponse getWorkDetail(Long workId) {
        // 필요정보 : 멤버, 업무, 업무체크리스트, 댓글
        // 1. 업무
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        // 2. 멤버 -> 신청자, 담당자
        Member producer = memberRepository.findById(work.getProducerId().getId())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        Member consumer = memberRepository.findById(work.getConsumerId().getId())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        // 3. 업무 체크리스트
        // TODO : 업무체크리스트 목록 조회 구현 후 추가
//        List<WorkCheckList> workCheckLists = workCheckListRepository.findAllByWork(work);

        // 4. 댓글
        // TODO : 댓글 목록 조회 구현 후 추가

//        return modelMapper.map(work, WorkListResponse.class);

        return WorkDetailResponse.builder()
                .workId(work.getId())
                .createdAt(work.getCreatedAt())
                .workStatusCode(CodeDetailSimpleResponse.toDto(work.getStatusCode()))
                .workTitle(work.getTitle())
                .workContent(work.getContent())
                .startDay(work.getStartDay())
                .endDay(work.getEndDay())
                .build();
    }

    @Override
    public void modifyWork(Long workId, UpdateWorkRequest workRequest) {
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        CodeDetail statusCode = codeDetailRepository.findById(workRequest.statusCode())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        Member consumer = memberRepository.findById(workRequest.consumerId())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        work.updateWork(statusCode, consumer, workRequest.title(), workRequest.content(), workRequest.startDay(), workRequest.endDay());
    }

    @Override
    public void modifyWorkStatus(Long workId, Integer statusCode) {
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        CodeDetail status = codeDetailRepository.findById(statusCode)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        work.updateWorkStatus(status);
    }

    @Override
    public void removeWork(Long workId) {
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        List<WorkCheckList> workCheckLists = workCheckListRepository.findAllByWork(work);

        // 1. 업무체크리스트 삭제
//        if (!workCheckLists.isEmpty()) {
//            log.debug("업무체크리스트 삭제");
//            workCheckListRepository.deleteAll(workCheckLists);
//        }

        // 2. 댓글 삭제처리 -> isDeleted = true로 update

        // 3. 업무 삭제
        log.debug("업무 삭제");
//        workRepository.delete(work);

    }

}