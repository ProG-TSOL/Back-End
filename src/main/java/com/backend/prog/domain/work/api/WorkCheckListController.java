package com.backend.prog.domain.work.api;

import com.backend.prog.domain.work.application.WorkCheckListService;
import com.backend.prog.domain.work.dto.CheckListResponse;
import com.backend.prog.domain.work.dto.CreateCheckListRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/worklist")
@RequiredArgsConstructor
public class WorkCheckListController {

    private final WorkCheckListService workCheckListService;

    @PostMapping
    public void saveCheckList(@Valid @RequestBody CreateCheckListRequest request) {
        workCheckListService.saveCheckList(request);
    }

    @GetMapping("/{workId}")
    public List<CheckListResponse> getCheckList(@PathVariable Long workId) {
        return workCheckListService.getCheckList(workId);
    }

    @PatchMapping("/{checkListId}")
    public void modifyCheckList(@PathVariable Long checkListId, @RequestBody HashMap<String, String> param) {
        workCheckListService.modfiyCheckList(checkListId, param.get("title"));
    }

    @PatchMapping("/{workId}/{checkListId}")
    public void checkFinish(@PathVariable Long checkListId, @RequestBody HashMap<String, Boolean> param) {
        workCheckListService.checkFinish(checkListId, param.get("isFinished"));
    }

    @DeleteMapping("/{checkListId}")
    public void removeCheckList(@PathVariable Long checkListId) {
        workCheckListService.removeCheckList(checkListId);
    }
}
