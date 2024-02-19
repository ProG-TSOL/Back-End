package com.backend.prog.domain.work.dto;

import com.backend.prog.domain.comment.dto.CommentDto;
import com.backend.prog.domain.manager.dto.CodeDetailSimpleResponse;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class WorkDetailResponse {
    // 멤버
    private Long workId; // 업무ID
    private LocalDateTime createdAt; // 업무 생성일시
    private CodeDetailSimpleResponse workStatusCode;
    private String workTitle;
    private String workContent;
    private LocalDate startDay; // 업무시작일
    private LocalDate endDay; // 업무종료일

    // 업무체크리스트
    private List<CheckListResponse> checkList;

    // 댓글
    private List<CommentDto.Response> comments;

    @Builder
    private WorkDetailResponse(Long workId, LocalDateTime createdAt, CodeDetailSimpleResponse workStatusCode, String workTitle, String workContent,
                               LocalDate startDay, LocalDate endDay, List<CheckListResponse> checkList, List<CommentDto.Response> comments) {
        this.workId = workId;
        this.createdAt = createdAt;
        this.workStatusCode = workStatusCode;
        this.workTitle = workTitle;
        this.workContent = workContent;
        this.startDay = startDay;
        this.endDay = endDay;
        this.checkList = checkList;
        this.comments = comments;
    }
}
