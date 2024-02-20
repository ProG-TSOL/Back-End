package com.backend.prog.domain.retrospect.application;


import com.backend.prog.domain.feed.application.FeedServiceimpl;
import com.backend.prog.domain.manager.dao.CodeDetailRepository;
import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.member.dao.MemberRepository;
import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.domain.project.dao.ProjectRespository;
import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.domain.retrospect.dao.RetrospectRepository;
import com.backend.prog.domain.retrospect.domain.Retrospect;
import com.backend.prog.domain.retrospect.dto.RetrospectDetailResponse;
import com.backend.prog.domain.retrospect.dto.RetrospectLatestResponse;
import com.backend.prog.domain.retrospect.dto.RetrospectSaveRequest;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RetrospectServiceImpl implements RetrospectService {

    private final RetrospectRepository retrospectRepository;
    private final ProjectRespository projectRespository;
    private final MemberRepository memberRepository;
    private final CodeDetailRepository codeDetailRepository;

    private final FeedServiceimpl feedServiceimpl;

    @Override
    @Transactional
    public void saveRetrospect(RetrospectSaveRequest request) {
        // 프로젝트, 멤버, KPT코드
        Project project = projectRespository.findById(request.projectId())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        CodeDetail kptCode = codeDetailRepository.findById(request.kptCode())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        // 1. 회고 저장
        Retrospect entity = Retrospect.builder()
                .project(project)
                .member(member)
                .kptCode(kptCode)
                .week(request.week())
                .content(request.content())
                .build();
        retrospectRepository.save(entity);

        // 2. 피드 생성
        Map<String, Object> feedDtoMap = Map.of(
                "projectId", project.getId(),
                "contentsId", entity.getId(),
                "memberId", member.getId()
        );
        feedServiceimpl.makeFeedDto("Retrospect", feedDtoMap);
    }

    @Override
    public Map<String, List<RetrospectLatestResponse>> getLatestRetrospects(Long projectId) {
        Project project = projectRespository.findById(projectId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        List<Retrospect> retrospects = retrospectRepository.getLatestRetrospects(project);

        // K, P, T 코드에 맞게 분류하기
        return retrospects.stream()
                .map(entity -> new RetrospectLatestResponse().toDto(entity))
                .collect(Collectors.groupingBy(RetrospectLatestResponse::getKptCodeName));
    }

    @Override
    public RetrospectDetailResponse getRetrospectDetail(Long retrospectId) {
        Retrospect retrospect = retrospectRepository.findById(retrospectId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        return new RetrospectDetailResponse().toDto(retrospect);
    }

    @Override
    @Transactional
    public void modifyRetrospect(Long retrospectId, String content) {
        Retrospect retrospect = retrospectRepository.findById(retrospectId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        retrospect.updateContent(content);
    }

    @Override
    @Transactional
    public void removeRetrospect(Long retrospectId) {
        Retrospect retrospect = retrospectRepository.findById(retrospectId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        retrospectRepository.delete(retrospect);
    }
}
