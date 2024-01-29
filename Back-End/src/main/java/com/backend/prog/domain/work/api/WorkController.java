package com.backend.prog.domain.work.api;

import com.backend.prog.domain.work.application.WorkService;
import com.backend.prog.domain.work.dto.CreateWorkRequest;
import com.backend.prog.domain.work.dto.WorkListResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/works")
@RequiredArgsConstructor
public class WorkController {

    private final WorkService workService;
    // TODO : work 컨트롤러 개발

    /**
     * 업무 등록
     */
    @PostMapping
    public void saveWork(@Valid @RequestBody CreateWorkRequest request) {
        workService.saveWork(request);
    }

    // TODO : 업무 목록 보여지는 항목에 따라 dto 세부 작성 -> WorkListReponse, WorkDetailResponse => 프론트와 협의
    @GetMapping("/{projectId}")
    public List<WorkListResponse> getWorkList(@PathVariable Long projectId) {
        return workService.getWorkList(projectId);
    }

    @GetMapping("/details/{workId}")
    public WorkListResponse getWork(@PathVariable Long workId) {
        return workService.getWork(workId);
    }

    @DeleteMapping("/{workId}")
    public void deleteWork(@NotNull @PathVariable Long workId) {
        workService.removeWork(workId);
    }
}
