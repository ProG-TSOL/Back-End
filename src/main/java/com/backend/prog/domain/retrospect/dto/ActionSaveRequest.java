package com.backend.prog.domain.retrospect.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public record ActionSaveRequest(@NotNull Long projectId, List<Map<String,String>> contents, @NotNull Integer week) {

}
