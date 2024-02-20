package com.backend.prog.domain.manager.api;

import com.backend.prog.domain.manager.application.CodeService;
import com.backend.prog.domain.manager.dto.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/codes")
public class CodeController {
    private final CodeService codeService;

    @PostMapping
    public void saveCode(@Valid @RequestBody CreateCodeRequest code) {
        printLog("saveCode");
        codeService.saveCode(code);
    }

    @PatchMapping
    public void modifyCode(@Valid @RequestBody UpdateCodeRequest code) {
        printLog("modifyCode");
        codeService.modifyCode(code);
    }

    @GetMapping
    public List<CodeResponse> getCodeList() {
        printLog("getCodeList");
        return codeService.getCodeList();
    }

    @GetMapping("/{codeName}")
    public CodeResponse getCode(@PathVariable("codeName") String codeName) {
        printLog("getCode");
        return codeService.getCodeByName(codeName);
    }

    @PostMapping("/details")
    public void saveCodeDetail(@Valid @RequestBody CreateCodeDetailRequest codeDetail) {
        printLog("saveCodeDetail");
        codeService.saveCodeDetail(codeDetail);
    }

    @PatchMapping("/details")
    public void modifyCodeDetail(@Valid @RequestBody UpdateCodeDetailRequest codeDetail) {
        printLog("modifyCodeDetail");
        codeService.modifyCodeDetail(codeDetail);
    }

    @GetMapping("/details/{codeName}")
    public List<CodeDetailSimpleResponse> getCodeDetailList(@PathVariable("codeName") String codeName) {
        printLog("getCodeDetailList");
        return codeService.getCodeDetailList(codeName);
    }

    @GetMapping("/details/detail/{codedetailId}")
    public CodeDetailResponse getCodeDetail(@NotNull @PathVariable("codedetailId") Integer codedetailId){
        printLog("getCodeDetail");
        return codeService.getCodeDetail(codedetailId);
    }

    private void printLog(String excetuionMethod) {

    }

}
