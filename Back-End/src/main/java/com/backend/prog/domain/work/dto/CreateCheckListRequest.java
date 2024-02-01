package com.backend.prog.domain.work.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateCheckListRequest(@NotNull Long workId, @NotEmpty String title) {
}
