package com.backend.prog.domain.retrospect.application;

import com.backend.prog.domain.retrospect.dto.RetrospectDetailResponse;
import com.backend.prog.domain.retrospect.dto.RetrospectLatestResponse;
import com.backend.prog.domain.retrospect.dto.RetrospectSaveRequest;

import java.util.List;
import java.util.Map;

public interface RetrospectService {

    void saveRetrospect(RetrospectSaveRequest request);

    Map<String, List<RetrospectLatestResponse>> getLatestRetrospects(Long projectId);

    RetrospectDetailResponse getRetrospectDetail(Long retrospectId);

    void modifyRetrospect(Long retrospectId, String content);

    void removeRetrospect(Long retrospectId);
}
