package com.backend.prog.domain.attendance.mapper;

import com.backend.prog.domain.attendance.domain.AttendanceLog;
import com.backend.prog.domain.attendance.dto.AttendanceLogDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class AttendanceLogMapper {
    public List<AttendanceLogDto.Response> entityToResponse(List<AttendanceLog> attendanceLogs) {
        return attendanceLogs.stream().map(al -> AttendanceLogDto.Response.builder()
                .id(al.getId())
                .startAt(LocalDateTime.of(
                        al.getStartAt().getYear(),
                        al.getStartAt().getMonth(),
                        al.getStartAt().getDayOfMonth(),
                        al.getStartAt().getHour(),
                        al.getStartAt().getMinute()
                ))
                .endAt(al.getEndAt() != null ? LocalDateTime.of(
                        al.getEndAt().getYear(),
                        al.getEndAt().getMonth(),
                        al.getEndAt().getDayOfMonth(),
                        al.getEndAt().getHour(),
                        al.getEndAt().getMinute()
                ) : null)
                .build()
        ).toList();
    }
}
