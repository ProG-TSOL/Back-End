package com.backend.prog.domain.attendance.dto;

import lombok.Builder;

import java.time.LocalDateTime;

public class AttendanceLogDto {
    public record Post() {

    }

    public record Patch() {

    }

    @Builder
    public record Response(Long id, LocalDateTime startAt, LocalDateTime endAt) {
    }

    @Builder
    public record SimpleResponse(boolean isWoking, Long id, LocalDateTime startAt) {
    }
}
