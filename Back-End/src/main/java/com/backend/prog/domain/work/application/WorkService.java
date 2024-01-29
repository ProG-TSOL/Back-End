package com.backend.prog.domain.work.application;

import com.backend.prog.domain.work.dto.CreateWorkRequest;
import com.backend.prog.domain.work.dto.SearchWorkRequest;
import com.backend.prog.domain.work.dto.UpdateWorkRequest;
import com.backend.prog.domain.work.dto.WorkListResponse;

import java.util.List;

public interface WorkService {

    void saveWork(CreateWorkRequest workRequest);

    List<WorkListResponse> getWorkList(Long projectId);
    List<WorkListResponse> searchByKeyword(SearchWorkRequest request);
    WorkListResponse getWork(Long workId);

    void modifyWork(Long workId, UpdateWorkRequest workRequest);
    void removeWork(Long workId);

}
