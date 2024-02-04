package com.backend.prog.domain.attendance.mapper;

import com.backend.prog.domain.attendance.domain.Attendance;
import com.backend.prog.domain.attendance.dto.AttendanceDto;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class AttendanceMapper {
    public List<AttendanceDto.Response> entityToResponses(List<Attendance> attendances) {
        return attendances.stream().map(attendance -> AttendanceDto.Response.builder()
                .id(attendance.getId())
                .workingDay(attendance.getWorkingDay())
                .workingTime(LocalTime.of(attendance.getWorkingTime().getHour(), attendance.getWorkingTime().getMinute()))
                .build()
        ).toList();
    }
}
