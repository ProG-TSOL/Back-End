package com.backend.prog.domain.retrospect.api;

import com.backend.prog.domain.retrospect.application.RetrospectService;
import com.backend.prog.domain.retrospect.dto.RetrospectDetailResponse;
import com.backend.prog.domain.retrospect.dto.RetrospectLatestResponse;
import com.backend.prog.domain.retrospect.dto.RetrospectSaveRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/retrospects")
@RequiredArgsConstructor
public class RetrospectController {
    private final RetrospectService retrospectService;

    @PostMapping
    public void saveRetrospect(@Valid @RequestBody RetrospectSaveRequest request) {
        retrospectService.saveRetrospect(request);
    }

    @GetMapping("/{projectId}")
    public Map<String, List<RetrospectLatestResponse>> getLatestRetrospects(@PathVariable Long projectId) {
        return retrospectService.getLatestRetrospects(projectId);
    }

    @GetMapping("/detail/{retrospectId}")
    public RetrospectDetailResponse getRetrospectDetail(@PathVariable Long retrospectId) {
        return retrospectService.getRetrospectDetail(retrospectId);
    }

    @PatchMapping("/{retrospectId}")
    public void modifyRetrospect(@PathVariable Long retrospectId, @RequestBody Map<String, String> request){
        retrospectService.modifyRetrospect(retrospectId, request.get("content"));
    }

    @DeleteMapping("/{retrospectId}")
    public void removeRetrospect(@PathVariable Long retrospectId){
        retrospectService.removeRetrospect(retrospectId);
    }

}
