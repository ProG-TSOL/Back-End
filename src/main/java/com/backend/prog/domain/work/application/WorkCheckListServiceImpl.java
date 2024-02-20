package com.backend.prog.domain.work.application;

import com.backend.prog.domain.work.dao.WorkCheckListRepository;
import com.backend.prog.domain.work.dao.WorkRepository;
import com.backend.prog.domain.work.domain.Work;
import com.backend.prog.domain.work.domain.WorkCheckList;
import com.backend.prog.domain.work.dto.CheckListResponse;
import com.backend.prog.domain.work.dto.CreateCheckListRequest;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkCheckListServiceImpl implements WorkCheckListService {

    private final WorkCheckListRepository workCheckListRepository;
    private final WorkRepository workRepository;
    private final ModelMapper modelMapper;

    @Override
    public void saveCheckList(CreateCheckListRequest request) {
        WorkCheckList entity = modelMapper.map(request, WorkCheckList.class);
        workCheckListRepository.save(entity);
    }

    @Override
    public List<CheckListResponse> getCheckList(Long workId) {
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        List<WorkCheckList> workCheckLists = workCheckListRepository.findAllByWork(work);

        List<CheckListResponse> result = workCheckLists.stream()
                .map(entity -> modelMapper.map(entity, CheckListResponse.class))
                .toList();


        return result;
    }

    @Override
    public void modfiyCheckList(Long checkListId, String title) {
        WorkCheckList entity = workCheckListRepository.findById(checkListId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        entity.update(title);
    }

    @Override
    public void checkFinish(Long checkListId, Boolean isFinished) {
        WorkCheckList entity = workCheckListRepository.findById(checkListId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        entity.checkFinish(isFinished);
    }

    @Override
    public void removeCheckList(Long checkListId) {
        WorkCheckList entity = workCheckListRepository.findById(checkListId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        workCheckListRepository.delete(entity);
    }


}
