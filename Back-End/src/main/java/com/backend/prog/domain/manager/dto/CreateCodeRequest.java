package com.backend.prog.domain.manager.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateCodeRequest(@NotEmpty String name, String description, @NotNull Boolean isUse) {
}