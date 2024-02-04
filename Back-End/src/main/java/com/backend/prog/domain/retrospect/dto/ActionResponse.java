package com.backend.prog.domain.retrospect.dto;

import com.backend.prog.domain.retrospect.domain.Action;
import lombok.Data;

@Data
public class ActionResponse {
    private Long actionId;
    private String content;
    private Integer week;

    public ActionResponse toDto(Action action) {
        this.actionId = action.getId();
        this.content = action.getContent();
        this.week = action.getWeek();
        return this;
    }
}
