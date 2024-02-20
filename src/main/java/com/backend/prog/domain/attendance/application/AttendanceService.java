package com.backend.prog.domain.attendance.application;

import com.backend.prog.domain.attendance.dao.AttendanceLogRepository;
import com.backend.prog.domain.attendance.dao.AttendanceRepository;
import com.backend.prog.domain.attendance.domain.Attendance;
import com.backend.prog.domain.attendance.domain.AttendanceLog;
import com.backend.prog.domain.attendance.dto.AttendanceDto;
import com.backend.prog.domain.attendance.dto.AttendanceLogDto;
import com.backend.prog.domain.attendance.mapper.AttendanceLogMapper;
import com.backend.prog.domain.attendance.mapper.AttendanceMapper;
import com.backend.prog.domain.project.dao.ProjectMemberRespository;
import com.backend.prog.domain.project.domain.ProjectMember;
import com.backend.prog.domain.project.domain.ProjectMemberId;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceLogRepository attendanceLogRepository;
    private final AttendanceRepository attendanceRepository;
    private final ProjectMemberRespository projectMemberRespository;
    private final AttendanceMapper attendanceMapper;
    private final AttendanceLogMapper attendanceLogMapper;

    public List<AttendanceDto.Response> getAttendance(Long projectId, Integer memberId, Integer month) {
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);
        //프로젝트에 멤버가 포함되있는지 검사
        getProjectMember(projectMemberId);
        List<Attendance> attendances = attendanceRepository.findAllByMonth(projectId, memberId, month);
        return attendanceMapper.entityToResponses(attendances);
    }

    public AttendanceLogDto.SimpleResponse startAttendanceLog(Long projectId, Integer memberId) {
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);

        //프로젝트에 멤버가 포함되있는지 검사
        ProjectMember projectMember = getProjectMember(projectMemberId);

        //출근중인지 확인(가장 최근 출근 일지 가져와서 퇴근시간이 null이 아니면 이미 출근 중인 상태)
        Optional<AttendanceLog> findLog = attendanceLogRepository.findByMember(projectId, memberId);
        if (findLog.isPresent() && findLog.get().getEndAt() == null) {
            throw new CommonException(ExceptionEnum.ALREADY_EXIST_STARTAT);
        }

        //출근 저장
        AttendanceLog attendanceLog = AttendanceLog.builder()
                .member(projectMember.getMember())
                .project(projectMember.getProject())
                .startAt(LocalDateTime.now())
                .build();

        attendanceLogRepository.save(attendanceLog);

        return AttendanceLogDto.SimpleResponse.builder()
                .isWoking(true)
                .id(attendanceLog.getId())
                .startAt(LocalDateTime.of(
                        attendanceLog.getStartAt().getYear(),
                        attendanceLog.getStartAt().getMonth(),
                        attendanceLog.getStartAt().getDayOfMonth(),
                        attendanceLog.getStartAt().getHour(),
                        attendanceLog.getStartAt().getMinute()
                ))
                .build();
    }

    @Transactional
    public void endAttendanceLog(Long projectId, Integer memberId) {
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);

        //프로젝트에 멤버가 포함되있는지 검사
        getProjectMember(projectMemberId);

        //출근중인지 확인(가장 최근 출근일지 가져와서 퇴근 시간이 null이 아니면 출근한적이 없다
        Optional<AttendanceLog> findLog = attendanceLogRepository.findByMember(projectId, memberId);

        if ((findLog.isPresent() && findLog.get().getEndAt() != null) || findLog.isEmpty()) {
            throw new CommonException(ExceptionEnum.ALREADY_EXIST_ENTAT);
        }

        AttendanceLog find = findLog.get();
        //퇴근 찍는 시간
        LocalDateTime endTime = LocalDateTime.now();
        //퇴근 시간 - 출근시간
        Timestamp start = Timestamp.valueOf(find.getStartAt());
        Timestamp end = Timestamp.valueOf(endTime);

        long workTime = ((end.getTime() - start.getTime()));

        // 출근 시간과 퇴근식간이 12시간 이상 차이나면 퇴근시간을 출근 시간 + 12시간 으로 재할당
        if (workTime / 1000 / 60 / 60 > 12) {
            endTime = find.getStartAt().plusHours(12);
        }

        Optional<Attendance> findAttendance = attendanceRepository.
                findByDay(find.getProject(), find.getMember(), find.getStartAt().toLocalDate());

        //두 날자가 같은 날이면 end 값 추가 후 저장
        if (find.getStartAt().toLocalDate().isEqual(endTime.toLocalDate())) {
            find.addEndAt(endTime);
            long hours = TimeUnit.MILLISECONDS.toHours(workTime);
            workTime -= TimeUnit.HOURS.toMillis(hours);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(workTime);
            workTime -= TimeUnit.MINUTES.toMillis(minutes);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(workTime);
            //출퇴근 날짜가 같고 그날에 이미 근무기록이 있으면
            if (findAttendance.isPresent()) {
                Attendance attendance = findAttendance.get();
                attendance.plusWorkingTime(hours, minutes, seconds);
            }
            //출퇴근 날짜가 같고 근무 기록이없으면 새로운 근무 기록 생성
            else {
                LocalTime workTimeLocal = LocalTime.of((int) hours % 24, (int) minutes, (int) seconds);

                Attendance attendance = Attendance.builder()
                        .project(find.getProject())
                        .member(find.getMember())
                        .workingDay(find.getStartAt().toLocalDate())
                        .workingTime(workTimeLocal)
                        .build();

                attendanceRepository.save(attendance);
            }
        }
        //같지 않으면 end값 start 날짜의
        else {
            //출근 로그 퇴근시간을 해당일 23:59:59
            find.addEndAt(find.getStartAt().withHour(23).withMinute(59).withSecond(59));

            //퇴근 시간 - 출근시간
            Timestamp start2 = Timestamp.valueOf(find.getStartAt());
            Timestamp end2 = Timestamp.valueOf(find.getEndAt());

            long workTime2 = ((end2.getTime() - start2.getTime()));
            long hours = TimeUnit.MILLISECONDS.toHours(workTime2);
            workTime2 -= TimeUnit.HOURS.toMillis(hours);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(workTime2);
            workTime2 -= TimeUnit.MINUTES.toMillis(minutes);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(workTime2);

            // 출근일 기준 근태 추가
            if (findAttendance.isPresent()) {
                Attendance attendance = findAttendance.get();
                attendance.plusWorkingTime(hours, minutes, seconds);
            } else {
                LocalTime workTimeLocal2 = LocalTime.of((int) hours % 24, (int) minutes, (int) seconds);

                Attendance attendanceStart = Attendance.builder()
                        .project(find.getProject())
                        .member(find.getMember())
                        .workingDay(find.getStartAt().toLocalDate())
                        .workingTime(workTimeLocal2)
                        .build();

                attendanceRepository.save(attendanceStart);
            }

            //퇴근일 기준 새로운 로그 생성 출근시간 00:00:00 퇴근시간 endTime
            AttendanceLog attendanceLog = AttendanceLog.builder()
                    .member(find.getMember())
                    .project(find.getProject())
                    .startAt(endTime.withHour(0).withMinute(0).withSecond(0))
                    .endAt(endTime)
                    .build();

            //퇴근 시간 - 출근시간
            Timestamp start3 = Timestamp.valueOf(find.getStartAt());
            Timestamp end3 = Timestamp.valueOf(find.getEndAt());

            long workTime3 = ((end3.getTime() - start3.getTime()));

            long hours3 = TimeUnit.MILLISECONDS.toHours(workTime3);
            workTime3 -= TimeUnit.HOURS.toMillis(hours3);
            long minutes3 = TimeUnit.MILLISECONDS.toMinutes(workTime3);
            workTime3 -= TimeUnit.MINUTES.toMillis(minutes3);
            long seconds3 = TimeUnit.MILLISECONDS.toSeconds(workTime3);

            LocalTime workTimeLocal3 = LocalTime.of((int) hours3 % 24, (int) minutes3, (int) seconds3);

            //퇴근일 근태 생성
            Attendance attendanceEnd = Attendance.builder()
                    .project(find.getProject())
                    .member(find.getMember())
                    .workingDay(find.getStartAt().toLocalDate())
                    .workingTime(workTimeLocal3)
                    .build();

            attendanceRepository.save(attendanceEnd);
            attendanceLogRepository.save(attendanceLog);
        }
    }

    public List<AttendanceLogDto.Response> getLogs(Long projectId, Integer memberId, Integer year, Integer month, Integer day) {
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);
        //프로젝트에 멤버가 포함되있는지 검사
        getProjectMember(projectMemberId);
        List<AttendanceLog> attendanceLogs = attendanceLogRepository.findAllByLocalDate(projectId, memberId, year, month, day);

        return attendanceLogMapper.entityToResponse(attendanceLogs);
    }

    public AttendanceLogDto.SimpleResponse startTime(Long projectId, Integer memberId) {
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);

        //프로젝트에 멤버가 포함되있는지 검사
        getProjectMember(projectMemberId);

        //출근중인지 확인(가장 최근 출근 일지 가져와서 퇴근시간이 null이 아니면 이미 출근 중인 상태)
        Optional<AttendanceLog> findLog = attendanceLogRepository.findByMember(projectId, memberId);
        AttendanceLogDto.SimpleResponse response;
        if (findLog.isPresent() && findLog.get().getEndAt() == null) {
            response = AttendanceLogDto.SimpleResponse.builder()
                    .isWoking(true)
                    .id(findLog.get().getId())
                    .startAt(LocalDateTime.of(
                            findLog.get().getStartAt().getYear(),
                            findLog.get().getStartAt().getMonth(),
                            findLog.get().getStartAt().getDayOfMonth(),
                            findLog.get().getStartAt().getHour(),
                            findLog.get().getStartAt().getMinute()
                    ))
                    .build();
        } else {
            response = AttendanceLogDto.SimpleResponse.builder()
                    .isWoking(false)
                    .build();
        }
        return response;
    }

    /**
     * 회원 프로젝트에 포함되어있는지 검사
     *
     * @param projectMemberId 프로젝트 멤버 아이디
     * @return ProjectMember 프로젝트 참여 멤버
     */
    private ProjectMember getProjectMember(ProjectMemberId projectMemberId) {
        return projectMemberRespository.findById(projectMemberId).orElseThrow(() -> new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE));
    }
}
