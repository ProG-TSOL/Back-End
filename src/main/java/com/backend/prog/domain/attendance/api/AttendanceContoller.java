package com.backend.prog.domain.attendance.api;

import com.backend.prog.domain.attendance.application.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attendances")
public class AttendanceContoller {

    private final AttendanceService attendanceService;

    @GetMapping("{project-id}/{member-id}")
    public Object getAttendances(@PathVariable("project-id") Long projectId,
                                               @PathVariable("member-id") Integer memberId,
                                               @RequestParam("month") Integer month) {
        return attendanceService.getAttendance(projectId, memberId, month);
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
        return attendanceService.getLogs(projectId, memberId, year, month, day);
    }

    @GetMapping("logs/{project-id}/{member-id}/startTime")
    public Object getStartTime(@PathVariable("project-id") Long projectId,
                                        @PathVariable("member-id") Integer memberId){
        return attendanceService.startTime(projectId, memberId);
    }
}
