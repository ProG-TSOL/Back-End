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
import com.backend.prog.domain.work.dto.CreateWorkRequest;
import com.backend.prog.domain.work.dto.SearchWorkRequest;
import com.backend.prog.domain.work.dto.UpdateWorkRequest;
import com.backend.prog.domain.work.dto.WorkListResponse;
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


    // ########################## 업무 start ##########################
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
                .map(entity -> modelMapper.map(entity, WorkListResponse.class))
                .toList();
    }

    // TODO : 검색 처리
    @Override
    public List<WorkListResponse> searchByKeyword(SearchWorkRequest request) {
        return null;
    }

    @Override
    public WorkListResponse getWork(Long workId) {
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        return modelMapper.map(work, WorkListResponse.class);
    }

    @Override
    public void modifyWork(Long workId, UpdateWorkRequest workRequest) {

    }

    @Override
    public void removeWork(Long workId) {
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        List<WorkCheckList> workCheckLists = workCheckListRepository.findAllByWork(work);

        // 1. 업무체크리스트 삭제
        if (!workCheckLists.isEmpty()) {
            log.debug("업무체크리스트 삭제");
            workCheckListRepository.deleteAll(workCheckLists);
        }

        // 2. 업무 삭제
        log.debug("업무 삭제");
        workRepository.delete(work);


    }

    // ########################## 업무 start ##########################

    // ########################## 업무 체크리스트 start ##########################
    // TODO: 업무 체크리스트 등록, 수정, 삭제, 목록 조회



    // ########################## 업무 체크리스트 end ##########################

}
