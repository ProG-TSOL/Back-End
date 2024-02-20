package com.backend.prog.domain.project.application;

import com.backend.prog.domain.manager.application.CodeService;
import com.backend.prog.domain.project.dao.ProjectMemberRespository;
import com.backend.prog.domain.project.dao.ProjectTotalRespository;
import com.backend.prog.domain.project.domain.ProjectCodeDetaliId;
import com.backend.prog.domain.project.domain.ProjectMember;
import com.backend.prog.domain.project.domain.ProjectMemberId;
import com.backend.prog.domain.project.domain.ProjectTotal;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectTotalService {
    private final ProjectTotalRespository projectTotalRespository;
    private final ProjectMemberRespository projectMemberRespository;
    private final CodeService codeService;
    private final ModelMapper mapper;

    public void createProjectTotalList(List<ProjectTotal> projectTotalDtoList) {
        projectTotalRespository.saveAll(projectTotalDtoList);
    }

    public Optional<ProjectTotal> getTotal(ProjectCodeDetaliId id) {
        return projectTotalRespository.findById(id);
    }

    public void updateProjectTotal(ProjectTotal projectTotal) {
        projectTotalRespository.save(projectTotal);
    }

    public void deleteProject(Long projectId, Integer memberId, Integer jobCode) {
        ProjectCodeDetaliId projectCodeDetaliId = new ProjectCodeDetaliId(projectId, jobCode);

        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);

        Optional<ProjectMember> projectMember = projectMemberRespository.findById(projectMemberId);

        if(!projectMember.isPresent() || projectMember.get().getRoleCode().getId() != 48){
            throw new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE);
        }

        projectTotalRespository.deleteById(projectCodeDetaliId);
    }
}
