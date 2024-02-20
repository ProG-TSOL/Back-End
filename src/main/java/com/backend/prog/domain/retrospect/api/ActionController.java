package com.backend.prog.domain.retrospect.api;

import com.backend.prog.domain.retrospect.application.ActionService;
import com.backend.prog.domain.retrospect.dto.ActionResponse;
import com.backend.prog.domain.retrospect.dto.ActionSaveRequest;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/actions")
@RequiredArgsConstructor
public class ActionController {
    private final ActionService actionService;

    @PostMapping
    public void saveAction(@Valid @RequestBody ActionSaveRequest request) {
        if (request.contents() == null) {
            throw new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST);
        }

        if (request.contents().size() > 3){
            throw new CommonException(ExceptionEnum.ONLY_REGISTER_THREE);
        }
        actionService.saveAction(request.projectId(), request.contents(), request.week());
    }

    @PatchMapping("/{actionId}")
    public void modifyAction(@PathVariable Long actionId, @RequestBody Map<String, String> param) {
        actionService.modifyAction(actionId, param.get("content"));
    }

    @DeleteMapping
    public void removeAction(Long actionId) {
        actionService.removeAction(actionId);
    }

    @GetMapping("/{projectId}")
    public List<ActionResponse> getLatestAction(@PathVariable Long projectId) {
        return actionService.getLatestAction(projectId);
    }
}
