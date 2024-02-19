package com.backend.prog.domain.retrospect.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RetrospectModifyRequest(@NotNull Long retrospectId,
                                      @NotEmpty String content) {
}
