package com.backend.prog.domain.project.application;

import com.backend.prog.domain.manager.application.CodeService;
import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.project.dao.ProjectTotalRespository;
import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.domain.project.domain.ProjectCodeDetaliId;
import com.backend.prog.domain.project.domain.ProjectTechCode;
import com.backend.prog.domain.project.domain.ProjectTotal;
import com.backend.prog.domain.project.dto.JobCodeTotalDto;
import com.backend.prog.domain.project.dto.ProjectTotalDtoSample;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProjectTotalService {
    private final ProjectTotalRespository projectTotalRespository;
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

    public void deleteProject(Long projectId, Integer jobCode) {
        ProjectCodeDetaliId projectCodeDetaliId = new ProjectCodeDetaliId(projectId, jobCode);
        projectTotalRespository.deleteById(projectCodeDetaliId);
    }
}
