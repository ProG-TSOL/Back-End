package com.backend.prog.domain.work.dto;

import com.backend.prog.domain.manager.dto.CodeDetailSimpleResponse;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString(exclude = {"statusCode", "typeCode", "priorityCode"})
@NoArgsConstructor
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

    @Builder
    private WorkListResponse(Long workId, CodeDetailSimpleResponse statusCode, CodeDetailSimpleResponse typeCode, CodeDetailSimpleResponse priorityCode, String producerMemberName, String title, LocalDate startDay, LocalDate endDay) {
        this.workId = workId;
        this.statusCode = statusCode;
        this.typeCode = typeCode;
        this.priorityCode = priorityCode;
        this.producerMemberName = producerMemberName;
        this.title = title;
        this.startDay = startDay;
        this.endDay = endDay;
    }
}
