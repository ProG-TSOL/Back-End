package com.backend.prog.domain.project.application;

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
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProjectMemberService {
    private final ProjectMemberRespository projectMemberRespository;
    private final ProjectMemberMapper projectMemberMapper;
    private final ProjectTotalRespository projectTotalRespository;

    public void createProjectMember(ProjectMember projectMember){
        projectMemberRespository.save(projectMember);
    }

    public Optional<ProjectMember> getProjectMember(ProjectMemberId projectMemberId) {
        return projectMemberRespository.findById(projectMemberId);
    }

    public List<ProjectMemberDto.Response> getProjectMembers(Long projecId) {
        List<ProjectMember> projectMembers = projectMemberRespository.findAllByProjectId(projecId);

        log.info("{}", projectMembers);

        return projectMemberMapper.entityToResponse(projectMembers);
    }

    public void deleteProjectMember(Long projectId, Integer memberId, Integer exiledId) {
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);

        ProjectMember projectMember = projectMemberRespository.findById(projectMemberId).orElseThrow(()-> new CommonException(ExceptionEnum.MEMBER_NOT_FOUND));

        //삭제 신청한 사람이 자기자신인 혹은 팀장 권한의 멤버인지 검사
        if(!projectMember.getRoleCode().getId().equals(17) || memberId != exiledId){
            log.info("memid {}", memberId);
            log.info("exid {}", exiledId);
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
}