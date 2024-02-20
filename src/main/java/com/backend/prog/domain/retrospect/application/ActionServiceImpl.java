package com.backend.prog.domain.retrospect.application;

import com.backend.prog.domain.project.dao.ProjectRespository;
import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.domain.retrospect.dao.ActionRepository;
import com.backend.prog.domain.retrospect.domain.Action;
import com.backend.prog.domain.retrospect.dto.ActionResponse;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ActionServiceImpl implements ActionService {

    private final ActionRepository actionRepository;
    private final ProjectRespository projectRespository;

    @Override
    @Transactional
    public void saveAction(Long projectId, List<Map<String, String>> contents, Integer week) {
        Project project = projectRespository.findById(projectId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        contents.forEach(
                map -> {
                    Action action = Action.builder()
                            .project(project)
                            .content(map.get("content"))
                            .week(week)
                            .build();
                    actionRepository.save(action);
                }
        );
    }

    @Override
    @Transactional
    public void modifyAction(Long actionId, String content) {
        Action action = actionRepository.findById(actionId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        action.updateContent(content);
    }

    @Override
    @Transactional
    public void removeAction(Long actionId) {
        Action action = actionRepository.findById(actionId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        actionRepository.delete(action);
    }

    @Override
    public List<ActionResponse> getLatestAction(Long projectId) {
        Project project = projectRespository.findById(projectId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        List<Action> actionList = actionRepository.findLatestAction(project);

        return actionList.stream()
                .map(entity -> new ActionResponse().toDto(entity))
                .toList();
    }
}
