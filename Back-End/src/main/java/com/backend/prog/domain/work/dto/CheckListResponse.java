package com.backend.prog.domain.work.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
public class CheckListResponse {
    private Long checkListId;
    private String title;
    private Boolean isFinished;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
