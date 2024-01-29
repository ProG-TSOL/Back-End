package com.backend.prog.domain.project.application;

import com.backend.prog.domain.project.dao.ProjectTechCodeRespository;
import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.domain.project.domain.ProjectCodeDetaliId;
import com.backend.prog.domain.project.domain.ProjectTechCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProjectTechCodeService {

    private final ProjectTechCodeRespository projectTechCodeRespository;

    public void createProjectTechCodeList(List<ProjectTechCode> list){
        projectTechCodeRespository.saveAll(list);
    }

    public Optional<ProjectTechCode> getTech(ProjectCodeDetaliId id) {
        return projectTechCodeRespository.findById(id);
    }

    public void updateProjectTechCode(ProjectTechCode projectTechCode) {
        projectTechCodeRespository.save(projectTechCode);
    }

    public void deleteProject(Long projectId, Integer techCode) {
        ProjectCodeDetaliId projectCodeDetaliId = new ProjectCodeDetaliId(projectId, techCode);
        projectTechCodeRespository.deleteById(projectCodeDetaliId);
    }
}
