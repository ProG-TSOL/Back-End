package com.backend.prog.domain.project.application;

import com.backend.prog.domain.project.dao.ProjectMemberRespository;
import com.backend.prog.domain.project.dao.ProjectTechCodeRespository;
import com.backend.prog.domain.project.domain.ProjectCodeDetaliId;
import com.backend.prog.domain.project.domain.ProjectMember;
import com.backend.prog.domain.project.domain.ProjectMemberId;
import com.backend.prog.domain.project.domain.ProjectTechCode;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectTechCodeService {

    private final ProjectTechCodeRespository projectTechCodeRespository;
    private final ProjectMemberRespository projectMemberRespository;

    public void createProjectTechCodeList(List<ProjectTechCode> list){
        projectTechCodeRespository.saveAll(list);
    }

    public Optional<ProjectTechCode> getTech(ProjectCodeDetaliId id) {
        return projectTechCodeRespository.findById(id);
    }

    public void updateProjectTechCode(ProjectTechCode projectTechCode) {
        projectTechCodeRespository.save(projectTechCode);
    }

    public void deleteProject(Long projectId, Integer memberId, Integer techCode) {
        ProjectCodeDetaliId projectCodeDetaliId = new ProjectCodeDetaliId(projectId, techCode);
        
        //요청한 멤버가 팀장인지 확인
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);

        Optional<ProjectMember> projectMember = projectMemberRespository.findById(projectMemberId);

        if(!projectMember.isPresent() || projectMember.get().getRoleCode().getId() != 48){
            throw new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE);
        }

        projectTechCodeRespository.deleteById(projectCodeDetaliId);
    }
}
