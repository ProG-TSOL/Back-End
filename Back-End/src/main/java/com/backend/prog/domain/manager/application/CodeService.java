package com.backend.prog.domain.manager.application;

import com.backend.prog.domain.manager.dto.*;

import java.util.List;

public interface CodeService {

    List<CodeResponse> getCodeList();
    CodeResponse getCodeByName(String codeName);
    void saveCode(CreateCodeRequest code);
    void modifyCode(UpdateCodeRequest code);

    List<CodeDetailSimpleResponse> getCodeDetailList(String codeName);
    CodeDetailResponse getCodeDetail(Integer codeId);
    void saveCodeDetail(CreateCodeDetailRequest codeDetail);
    void modifyCodeDetail(UpdateCodeDetailRequest codeDetail);

}
