package com.backend.prog.domain.project.application;

import com.backend.prog.domain.project.dao.ProjectMemberRespository;
import com.backend.prog.domain.project.domain.ProjectMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProjectMemberService {
    private final ProjectMemberRespository projectMemberRespository;

    public void createProjectMember(ProjectMember projectMember){
        projectMemberRespository.save(projectMember);
    }

}
