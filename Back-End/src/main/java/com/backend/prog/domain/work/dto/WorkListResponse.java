package com.backend.prog.domain.work.dto;

import com.backend.prog.domain.manager.dto.CodeDetailSimpleResponse;
import lombok.Data;

import java.time.LocalDate;

@Data
public class WorkListResponse {
    private Long workId;

    private CodeDetailSimpleResponse statusCode;
    private CodeDetailSimpleResponse typeCode;
    private CodeDetailSimpleResponse priorityCode;

    // 담당자 회원ID, 담당자명
    private String producerMemberName;

    private String title;
    private LocalDate startDay;
    private LocalDate endDay;

}
