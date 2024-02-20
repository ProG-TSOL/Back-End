package com.backend.prog.domain.project.application;

import com.backend.prog.domain.manager.dao.CodeDetailRepository;
import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.member.dao.MemberRepository;
import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.domain.project.dao.ApplicationStatusRepository;
import com.backend.prog.domain.project.dao.ProjectMemberRespository;
import com.backend.prog.domain.project.dao.ProjectRespository;
import com.backend.prog.domain.project.dao.ProjectTotalRespository;
import com.backend.prog.domain.project.domain.*;
import com.backend.prog.domain.project.dto.ApplicationStatusDto;
import com.backend.prog.domain.project.mapper.ApplicationStatusMapper;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationStatusService {
    private final ApplicationStatusRepository applicationStatusRepository;
    private final ProjectRespository projectRespository;
    private final MemberRepository memberRepository;
    private final CodeDetailRepository codeDetailRepository;
    private final ProjectMemberRespository projectMemberRespository;
    private final ProjectTotalRespository projectTotalRespository;
    private final ApplicationStatusMapper applicationStatusMapper;

    public void createApplicationStatus(Long projectId, Integer memberId, Integer jobCode) {

        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);

        Project project = projectRespository.findById(projectId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        CodeDetail codeDetail = codeDetailRepository.findById(jobCode).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        //프로젝트에 해당 잡코드가 있는지 검사
        ProjectCodeDetaliId projectCodeDetaliId = new ProjectCodeDetaliId(projectId, jobCode);
        ProjectTotal projectTotal = projectTotalRespository.findById(projectCodeDetaliId).orElseThrow(() -> new CommonException(ExceptionEnum.NOT_HAVE_POSITION));

        // 이미 꽉차있는 포지션이면 에러 발생
        if(projectTotal.getTotal() <= projectTotal.getCurrent()){
            throw new CommonException(ExceptionEnum.POSITION_FULL);
        }

        //이미 참여중인 프로젝트인지 검사
        Optional<ProjectMember> findProjectMember = projectMemberRespository.findById(projectMemberId);
        //이미 신청한 프로젝트 인지 검사
        Optional<ApplicationStatus> findApplication = applicationStatusRepository.findById(projectMemberId);

        if(findProjectMember.isPresent() || findApplication.isPresent()){
            throw new CommonException(ExceptionEnum.PROJECT_MEMBER);
        }

        ApplicationStatus applicationStatus = new ApplicationStatus(projectMemberId, project, member, codeDetail);

        applicationStatusRepository.save(applicationStatus);
    }

    @Transactional
    public void acceptMember(Long projectId, Integer memberId, Integer acceptMemberId) {

        ProjectMemberId plId = new ProjectMemberId(projectId, memberId);

        ProjectMember findPl = projectMemberRespository.findById(plId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        //요청한 멤버가 팀장인지 검사
        if(findPl.getRoleCode().getId() != 48){
            throw new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE);
        }

        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, acceptMemberId);

        ApplicationStatus applicationStatus = applicationStatusRepository.findById(projectMemberId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        //기본 역할 팀원으로 설정
        CodeDetail codeDetail = codeDetailRepository.findById(49).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        ProjectMember projectMember = new ProjectMember(projectMemberId, applicationStatus.getProject(), applicationStatus.getMember(), applicationStatus.getJobCode(), codeDetail);

        //projectTotal current +1
        ProjectCodeDetaliId projectCodeDetaliId = new ProjectCodeDetaliId(projectId, applicationStatus.getJobCode().getId());

        ProjectTotal projectTotal = projectTotalRespository.findById(projectCodeDetaliId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        if (projectTotal.getCurrent() < projectTotal.getTotal()) {
            projectTotal.addCurrent();
            projectTotalRespository.save(projectTotal);
        }else{
            throw new CommonException(ExceptionEnum.POSITION_FULL);
        }

        //프로젝트 멤버 저장
        projectMemberRespository.save(projectMember);

        //신청 현황 삭제
        applicationStatusRepository.deleteById(projectMemberId);
    }

    public void refuseMember(Long projectId, Integer memberId, Integer acceptMemberId) {

        if(memberId != acceptMemberId){
            ProjectMemberId plId = new ProjectMemberId(projectId, memberId);

            ProjectMember findPl = projectMemberRespository.findById(plId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

            if(findPl.getRoleCode().getId() != 48){
                throw new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE);
            }
        }

        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, acceptMemberId);

        //신청 현황 삭제
        applicationStatusRepository.deleteById(projectMemberId);
    }

    public List<ApplicationStatusDto.Response> getApplications(Long projectId, Integer memberId) {
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);

        ProjectMember findProjectMember = projectMemberRespository.findById(projectMemberId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        //팀장인지 확인
        if(!findProjectMember.getRoleCode().getId().equals(48)){
            throw new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE);
        }

        List<ApplicationStatus> list = applicationStatusRepository.findAllByProjectMember(projectId);

        return applicationStatusMapper.entityToSimpleResponses(list);
    }
}