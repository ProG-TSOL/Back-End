package com.backend.prog.domain.retrospect.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RetrospectSaveRequest(@NotNull Long projectId,
                                    @NotNull Integer memberId,
                                    @NotNull Integer kptCode,
                                    @NotNull Integer week,
                                    @NotEmpty String content) {
}
