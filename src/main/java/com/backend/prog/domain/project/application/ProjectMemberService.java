package com.backend.prog.domain.project.application;

import com.backend.prog.domain.manager.dao.CodeDetailRepository;
import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.project.dao.ProjectMemberRespository;
import com.backend.prog.domain.project.dao.ProjectTotalRespository;
import com.backend.prog.domain.project.domain.ProjectCodeDetaliId;
import com.backend.prog.domain.project.domain.ProjectMember;
import com.backend.prog.domain.project.domain.ProjectMemberId;
import com.backend.prog.domain.project.domain.ProjectTotal;
import com.backend.prog.domain.project.dto.ProjectMemberDto;
import com.backend.prog.domain.project.mapper.ProjectMemberMapper;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {
    private final ProjectMemberRespository projectMemberRespository;
    private final ProjectMemberMapper projectMemberMapper;
    private final ProjectTotalRespository projectTotalRespository;
    private final CodeDetailRepository codeDetailRepository;

    public void createProjectMember(ProjectMember projectMember){
        projectMemberRespository.save(projectMember);
    }

    public Optional<ProjectMember> getProjectMember(ProjectMemberId projectMemberId) {
        return projectMemberRespository.findById(projectMemberId);
    }

    public List<ProjectMemberDto.Response> getProjectMembers(Long projecId) {
        List<ProjectMember> projectMembers = projectMemberRespository.findAllByProjectId(projecId);


        return projectMemberMapper.entityToResponse(projectMembers);
    }

    public void deleteProjectMember(Long projectId, Integer memberId, Integer exiledId) {
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);

        ProjectMember projectMember = projectMemberRespository.findById(projectMemberId).orElseThrow(()-> new CommonException(ExceptionEnum.MEMBER_NOT_FOUND));

        //삭제 신청한 사람이 자기자신인 혹은 팀장 권한의 멤버인지 검사
        if(!projectMember.getRoleCode().getId().equals(48) && memberId != exiledId){
            throw new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE);
        }
        
        // 탈퇴, 추방할 멤버 조회
        ProjectMemberId deleteId = new ProjectMemberId(projectId, exiledId);

        ProjectMember deleteMember = projectMemberRespository.findById(deleteId).orElseThrow(()-> new CommonException(ExceptionEnum.MEMBER_NOT_FOUND));

        // 프로젝트 토탈 current -1
        ProjectCodeDetaliId projectCodeDetaliId = new ProjectCodeDetaliId(projectMember.getProject().getId(), projectMember.getJobCode().getId());
        ProjectTotal projectTotal = projectTotalRespository.findById(projectCodeDetaliId).orElseThrow(()-> new CommonException(ExceptionEnum.NOT_HAVE_POSITION));
        if(projectTotal.getCurrent() > 0){ //현재원이 0보다 클떄만 -1
            projectTotal.subtractCurrent();
        }

        projectTotalRespository.save(projectTotal);

        projectMemberRespository.delete(deleteMember);


    }

    @Transactional
    public void updateProjectMemver(Long projectId, Integer memberId, Integer moveMember, Integer moveJob) {
        ProjectMemberId pMId = new ProjectMemberId(projectId, memberId);

        Optional<ProjectMember> findPM = projectMemberRespository.findById(pMId);
        
        //존재하는 프로젝트 멤버인지 팀장인지 검사
        if(!findPM.isPresent() && !findPM.get().getRoleCode().getId().equals(48)){
            throw new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE);
        }
        ProjectCodeDetaliId projectCodeDetaliId = new ProjectCodeDetaliId(projectId, moveJob);

        //프로젝트에 포함된 포지션인지 확인
        ProjectTotal projectTotal = projectTotalRespository.findById(projectCodeDetaliId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        //포지션 이동할 멤버
        ProjectMemberId moveId = new ProjectMemberId(projectId, moveMember);
        ProjectMember movePM =  projectMemberRespository.findById(moveId).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));


        //현재원이 총원보다 작고 포지션 이동할 멤버가 이미 해당 포지션이 아니면 수정
        if(projectTotal.getTotal() > projectTotal.getCurrent() && moveJob != movePM.getJobCode().getId()){

            //멤버의 이동전 포지션의 프로젝트 토탈
            ProjectTotal fnidPM = projectTotalRespository.findById(new ProjectCodeDetaliId(projectId, movePM.getJobCode().getId())).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
            fnidPM.subtractCurrent();
            projectTotalRespository.save(fnidPM);

            CodeDetail findCodeDetail = codeDetailRepository.findById(moveJob).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
            //포지션 이동할 멤버가 기존에 있었던 포지션 총원 -1
            projectTotal.addCurrent();
            projectTotalRespository.save(projectTotal);

            movePM.updateJob(findCodeDetail);
        }else {
            throw new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST);
        }

    }
}