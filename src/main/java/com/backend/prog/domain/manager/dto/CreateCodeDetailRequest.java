package com.backend.prog.domain.manager.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateCodeDetailRequest(@NotNull Integer codeId,
                                      @NotEmpty String detailName,
                                      String detailDescription,
                                      String imgUrl
                                      ,@NotNull Boolean isUse) {
}
