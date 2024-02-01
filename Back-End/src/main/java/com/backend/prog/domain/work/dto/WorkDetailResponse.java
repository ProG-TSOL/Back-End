package com.backend.prog.domain.work.dto;

import com.backend.prog.domain.manager.dto.CodeDetailSimpleResponse;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
public class WorkDetailResponse {
    // TODO : 멤버, 업무체크리스트, 댓글 DTO 생성시 해당 DTO로 변경
    // 멤버
    // memberDto 신청자, 담당자

    private Long workId; // 업무ID
    private LocalDateTime createdAt; // 업무 생성일시
    private CodeDetailSimpleResponse workStatusCode;
    private String workTitle;
    private String workContent;
    private LocalDate startDay; // 업무시작일
    private LocalDate endDay; // 업무종료일

    // 업무체크리스트
    // 체크리스트Dto

    // 댓글
    // 댓글Dto


    @Builder
    private WorkDetailResponse(Long workId, LocalDateTime createdAt, CodeDetailSimpleResponse workStatusCode, String workTitle, String workContent, LocalDate startDay, LocalDate endDay) {
        this.workId = workId;
        this.createdAt = createdAt;
        this.workStatusCode = workStatusCode;
        this.workTitle = workTitle;
        this.workContent = workContent;
        this.startDay = startDay;
        this.endDay = endDay;
    }
}
