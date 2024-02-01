package com.backend.prog.domain.work.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateWorkRequest(@NotNull(message = "프로젝트ID는 필수값입니다.") Long projectId,
                                @NotNull Integer producerId,
                                @NotNull Integer statusCode,
                                @NotNull Integer typeCode,
                                @NotNull Integer priorityCode,
                                @NotNull Integer consumerId,
                                @NotEmpty String title,
                                String content,
                                @FutureOrPresent LocalDate startDay,
                                @Future LocalDate endDay){
}
