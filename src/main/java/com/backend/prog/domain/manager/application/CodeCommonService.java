package com.backend.prog.domain.manager.application;

import com.backend.prog.domain.manager.dao.CodeDetailRepository;
import com.backend.prog.domain.manager.dao.CodeRepository;
import com.backend.prog.domain.manager.domain.Code;
import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 내부적으로 사용하기 위한 공통 코드 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CodeCommonService {

    private final CodeDetailRepository codeDetailRepository;
    private final CodeRepository codeRepository;

    public CodeDetail getCodeDetail(Integer codeDetailId) {
        return codeDetailRepository.findById(codeDetailId)
                .orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));
    }

    public CodeDetail getCodeDetailByNames(String codeName, String codeDetailName) {
        Code code = codeRepository.findByName(codeName);
        return codeDetailRepository.findByCodeAndDetailName(code, codeDetailName);
    }
}
