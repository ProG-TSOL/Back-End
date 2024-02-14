package com.backend.prog.domain.work.dto;

import com.backend.prog.domain.work.domain.WorkCheckList;
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

    public CheckListResponse toDto(WorkCheckList entity) {
        this.checkListId = entity.getId();
        this.title = entity.getTitle();
        this.isFinished = entity.getIsFinish();
        this.createdAt = entity.getCreatedAt();
        this.modifiedAt = entity.getModifiedAt();
        return this;
    }
}
