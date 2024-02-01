package com.backend.prog.domain.work.application;

import com.backend.prog.domain.work.dto.CheckListResponse;
import com.backend.prog.domain.work.dto.CreateCheckListRequest;

import java.util.List;

public interface WorkCheckListService {

    void saveCheckList(CreateCheckListRequest request);
    List<CheckListResponse> getCheckList(Long workId);

    void modfiyCheckList(Long checkListId, String title);

    void checkFinish(Long checkListId, Boolean isFinished);

    void removeCheckList(Long checkListId);
}
