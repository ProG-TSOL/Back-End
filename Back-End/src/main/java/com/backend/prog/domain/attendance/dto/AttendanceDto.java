package com.backend.prog.domain.attendance.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

public class AttendanceDto {
    public record Post() {

    }

    public record Patch() {

    }

    @Builder
    public record Response(Long id, LocalDate workingDay, LocalTime workingTime) {
    }
}
