package com.backend.prog.domain.attendance.api;

import com.backend.prog.domain.attendance.application.AttendanceService;
import com.backend.prog.domain.attendance.dto.AttendanceDto;
import com.backend.prog.domain.attendance.dto.AttendanceLogDto;
import com.backend.prog.global.common.CommonApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/attendances")
public class AttendanceContoller {

    private final AttendanceService attendanceService;

    @GetMapping("{project-id}/{member-id}")
    public Object getAttendances(@PathVariable("project-id") Long projectId,
                                               @PathVariable("member-id") Integer memberId,
                                               @RequestParam("month") Integer month) {

        List<AttendanceDto.Response> responses = attendanceService.getAttendance(projectId, memberId, month);

        return responses;
    }

    @PostMapping("logs/{project-id}/{member-id}/start")
    public Object startAttendanceLog(@PathVariable("project-id") Long projectId,
                                                   @PathVariable("member-id") Integer memberId) {
        return attendanceService.startAttendanceLog(projectId, memberId);
    }

    @PatchMapping("logs/{project-id}/{member-id}/end")
    public void endAttendanceLog(@PathVariable("project-id") Long projectId,
                                                   @PathVariable("member-id") Integer memberId) {
        attendanceService.endAttendanceLog(projectId, memberId);
    }

    @GetMapping("logs/{project-id}/{member-id}")
    public Object getLogs(@PathVariable("project-id") Long projectId,
                                        @PathVariable("member-id") Integer memberId,
                                        @RequestParam("year") Integer year,
                                        @RequestParam("month") Integer month,
                                        @RequestParam("day") Integer day){
        List<AttendanceLogDto.Response> responses = attendanceService.getLogs(projectId, memberId, year, month, day);

        return responses;
    }

    @GetMapping("logs/{project-id}/{member-id}/startTime")
    public Object getStartTime(@PathVariable("project-id") Long projectId,
                                        @PathVariable("member-id") Integer memberId){

        AttendanceLogDto.SimpleResponse response = attendanceService.startTime(projectId, memberId);

        return response;
    }
}
