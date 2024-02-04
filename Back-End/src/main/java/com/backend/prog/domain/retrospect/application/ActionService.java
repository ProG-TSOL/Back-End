package com.backend.prog.domain.retrospect.application;

import com.backend.prog.domain.retrospect.dto.ActionResponse;

import java.util.List;
import java.util.Map;

public interface ActionService {

    void saveAction(Long projectId, List<Map<String,String>> contents, Integer week);

    void modifyAction(Long actionId, String content);

    void removeAction(Long actionId);

    List<ActionResponse> getLatestAction(Long projectId);
}
