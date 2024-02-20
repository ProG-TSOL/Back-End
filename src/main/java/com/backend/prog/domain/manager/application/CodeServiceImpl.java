package com.backend.prog.domain.manager.application;

import com.backend.prog.domain.manager.dao.CodeDetailRepository;
import com.backend.prog.domain.manager.dao.CodeRepository;
import com.backend.prog.domain.manager.domain.Code;
import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.manager.dto.*;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CodeServiceImpl implements CodeService {

    private final CodeDetailRepository codeDetailRepository;
    private final CodeRepository codeRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CodeResponse> getCodeList() {
        List<Code> codeList = codeRepository.findAll();
        checkDataExist(codeList);

        return codeList.stream()
                .map(entity -> modelMapper.map(entity, CodeResponse.class))
                .toList();
    }

    @Override
    public CodeResponse getCodeByName(String codeName) {
        Code code = codeRepository.findByName(codeName);
        checkDataExist(code);
        return modelMapper.map(code, CodeResponse.class);
    }

    @Override
    @Transactional
    public void saveCode(CreateCodeRequest param) {
        if (codeRepository.findByName(param.name()) != null) {
            throw new CommonException(ExceptionEnum.NAME_ALREADY_EXISTS);
        }
        Code entity = modelMapper.map(param, Code.class);
        codeRepository.save(entity);
    }

    @Override
    @Transactional
    public void modifyCode(UpdateCodeRequest code) {
        Code getCode = codeRepository.findById(code.id())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        getCode.update(code.name(), code.description(), code.isUse());
    }

    @Override
    public List<CodeDetailSimpleResponse> getCodeDetailList(String codeName) {
        Code code = codeRepository.findByName(codeName);
        List<CodeDetail> detailList = codeDetailRepository.findByCode(code);
        checkDataExist(detailList);

        return detailList.stream()
                .map(entity -> new CodeDetailSimpleResponse().toDto(entity))
                .toList();
    }

    @Override
    public CodeDetailResponse getCodeDetail(Integer codeId) {
        CodeDetail codeDetail = codeDetailRepository.findById(codeId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
        return modelMapper.map(codeDetail, CodeDetailResponse.class);
    }

    @Override
    @Transactional
    public void saveCodeDetail(CreateCodeDetailRequest codeDetail) {
        Code code = codeRepository.findById(codeDetail.codeId())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        CodeDetail detail = CodeDetail.builder()
                .code(code)
                .detailName(codeDetail.detailName())
                .detailDescription(codeDetail.detailDescription())
                .imgUrl(codeDetail.imgUrl())
                .build();

        if (codeDetailRepository.findByCodeAndDetailName(detail.getCode(), detail.getDetailName()) != null) {
            throw new CommonException(ExceptionEnum.NAME_ALREADY_EXISTS);
        }

        codeDetailRepository.save(detail);
    }

    @Override
    @Transactional
    public void modifyCodeDetail(UpdateCodeDetailRequest codeDetail) {
        CodeDetail entity = codeDetailRepository.findById(codeDetail.id())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        Code getCode = codeDetailRepository.findById(entity.getId())
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST)).getCode();

        if (codeDetailRepository.findByCodeAndDetailName(getCode, codeDetail.detailName()) != null) {
            throw new CommonException(ExceptionEnum.NAME_ALREADY_EXISTS);
        }
        entity.update(codeDetail.detailName(), codeDetail.detailDescription(), codeDetail.imgUrl(), codeDetail.isUse());
    }

    /**
     * 데이터 존재 유무 체크
     */
    public <T> void checkDataExist(T entity) {
        if (entity == null) {
            throw new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST);
        }
        if (entity instanceof Collection) {
            if (((Collection<?>) entity).isEmpty()) {
                throw new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST);
            }
        }
    }
}
