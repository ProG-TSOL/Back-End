package com.backend.prog.domain.work.application;

import com.backend.prog.domain.work.dto.*;

import java.util.List;

public interface WorkService {

    void saveWork(CreateWorkRequest workRequest);

    List<WorkListResponse> getWorkList(Long projectId);
    List<WorkListResponse> getWorkListSearchByKeyword(Long projectId, String title);
    WorkDetailResponse getWorkDetail(Long workId);

    void modifyWork(Long workId, UpdateWorkRequest workRequest);

    void modifyWorkStatus(Long workId, Integer statusCode);
    void removeWork(Long workId);

}
