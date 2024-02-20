package com.backend.prog.domain.work.api;

import com.backend.prog.domain.work.application.WorkService;
import com.backend.prog.domain.work.dto.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/works")
@RequiredArgsConstructor
public class WorkController {

    private final WorkService workService;

    @PostMapping
    public void saveWork(@Valid @RequestBody CreateWorkRequest request) {
        workService.saveWork(request);
    }

    @GetMapping("/{projectId}")
    public List<WorkListResponse> getWorkList(@PathVariable Long projectId) {
        return workService.getWorkList(projectId);
    }

    @GetMapping("/details/{workId}")
    public WorkDetailResponse getWork(@PathVariable Long workId) {
        return workService.getWorkDetail(workId);
    }

    @GetMapping("/search/{projectId}")
    public List<WorkListResponse> getWorkListSearchByKeyword(@PathVariable Long projectId, @NotEmpty String title) {
        return workService.getWorkListSearchByKeyword(projectId, title);
    }

    @DeleteMapping("/{workId}")
    public void deleteWork(@NotNull @PathVariable Long workId) {
        workService.removeWork(workId);
    }

    @PatchMapping("/{workId}")
    public void modifyWork(@PathVariable Long workId, @Valid @RequestBody UpdateWorkRequest workRequest) {
        workService.modifyWork(workId, workRequest);
    }

    @PatchMapping("/status/{workId}")
    public void modifyWorkStatus(@PathVariable Long workId, @RequestBody Map<String,Integer> statusCode) {
        workService.modifyWorkStatus(workId, statusCode.get("statusCode"));
    }


}
