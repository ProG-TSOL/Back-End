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
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Log4j2
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
        ProjectMember projectMember = projectMemberRespository.findById(projectMemberId).orElseThrow(() -> new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE));


        List<Attendance> attendances = attendanceRepository.findAllByMonth(projectId, memberId, month);

        return attendanceMapper.entityToResponses(attendances);
    }

    public void startAttendanceLog(Long projectId, Integer memberId) {
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);

        //프로젝트에 멤버가 포함되있는지 검사
        ProjectMember projectMember = projectMemberRespository.findById(projectMemberId).orElseThrow(() -> new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE));

        //출근중인지 확인(가장 최근 출근 일지 가져와서 퇴근시간이 null이 아니면 이미 출근 중인 상태)
        Optional<AttendanceLog> findLog = attendanceLogRepository.findByMember(projectId, memberId);
        if (findLog.isPresent() && findLog.get().getEndAt() == null){
            throw new CommonException(ExceptionEnum.ALREADY_EXIST_STARTAT);
        }

        //출근 저장
        AttendanceLog attendanceLog = AttendanceLog.builder()
                .member(projectMember.getMember())
                .project(projectMember.getProject())
                .startAt(LocalDateTime.now())
                .build();

        attendanceLogRepository.save(attendanceLog);
    }

    @Transactional
    public void endAttendanceLog(Long projectId, Integer memberId) {
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);

        //프로젝트에 멤버가 포함되있는지 검사
        ProjectMember projectMember = projectMemberRespository.findById(projectMemberId).orElseThrow(() -> new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE));

        //출근중인지 확인(가장 최근 출근일지 가져와서 퇴근 시간이 null이 아니면 출근한적이 없다
        Optional<AttendanceLog> findLog = attendanceLogRepository.findByMember(projectId, memberId);

        if ((findLog.isPresent() && findLog.get().getEndAt() != null) || findLog.isEmpty()){
            throw new CommonException(ExceptionEnum.ALREADY_EXIST_ENTAT);
        }

        AttendanceLog find = findLog.get();
        LocalDateTime endTime = LocalDateTime.now();
        Duration duration = Duration.between(find.getStartAt(), endTime);
        Long workTime = duration.toHours();
        
        // 출근 시간과 퇴근식간이 12시간 이상 차이나면 퇴근시간을 출근 시간 + 12시간 으로 재할당
        if(workTime > 12){
            endTime = find.getStartAt().plusHours(12);
        }

        Optional<Attendance> findAttendance = attendanceRepository.findByDay(find.getProject(), find.getMember(), find.getStartAt().toLocalDate());


        //두 날자가 같은 날이면 end 값 추가 후 저장
        if (find.getStartAt().toLocalDate().equals(endTime.toLocalDate())){
            find.addEndAt(endTime);
            Duration workTimes = Duration.between(find.getStartAt().toLocalTime(), find.getEndAt().toLocalTime());
            if (findAttendance.isPresent()){
                Attendance attendance = findAttendance.get();
                attendance.plusWorkingTime(workTimes);
            }else {
                Attendance attendance = Attendance.builder()
                        .project(find.getProject())
                        .member(find.getMember())
                        .workingDay(find.getStartAt().toLocalDate())
                        .workingTime(LocalTime.ofNanoOfDay(workTimes.toNanos()))
                        .build();

                attendanceRepository.save(attendance);
            }
        }
        //같지 않으면 end값 start 날짜의
        else {
            //출근 로그 퇴근시간을 해당일 23:59:59
            find.addEndAt(find.getStartAt().withHour(23).withMinute(59).withSecond(59));
            Duration workTimeStart = Duration.between(find.getStartAt().toLocalTime(), find.getEndAt().toLocalTime());
            // 출근일 기준 근태 추가
            if (findAttendance.isPresent()){
                Attendance attendance = findAttendance.get();
                attendance.plusWorkingTime(workTimeStart);
            }else {
                Attendance attendanceStart = Attendance.builder()
                        .project(find.getProject())
                        .member(find.getMember())
                        .workingDay(find.getStartAt().toLocalDate())
                        .workingTime(LocalTime.ofNanoOfDay(workTimeStart.toNanos()))
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
            Duration workTimeEnd = Duration.between(attendanceLog.getStartAt().toLocalTime(), attendanceLog.getEndAt().toLocalTime());
            //퇴근일 근태 생성
            Attendance attendancEnd = Attendance.builder()
                    .project(find.getProject())
                    .member(find.getMember())
                    .workingDay(find.getStartAt().toLocalDate())
                    .workingTime(LocalTime.ofNanoOfDay(workTimeEnd.toNanos()))
                    .build();

            attendanceRepository.save(attendancEnd);
            attendanceLogRepository.save(attendanceLog);
        }
    }

    public List<AttendanceLogDto.Response> getLogs(Long projectId, Integer memberId, Integer year, Integer month, Integer day) {
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);

        //프로젝트에 멤버가 포함되있는지 검사
        ProjectMember projectMember = projectMemberRespository.findById(projectMemberId).orElseThrow(() -> new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE));

        List<AttendanceLog> attendanceLogs = attendanceLogRepository.findAllByLocalDate(projectId, memberId, year, month, day);


        return attendanceLogMapper.entityToResponse(attendanceLogs);
    }

    public AttendanceLogDto.SimpleResponse startTime(Long projectId, Integer memberId) {
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);

        //프로젝트에 멤버가 포함되있는지 검사
        ProjectMember projectMember = projectMemberRespository.findById(projectMemberId).orElseThrow(() -> new CommonException(ExceptionEnum.AUTHORITY_NOT_HAVE));

        //출근중인지 확인(가장 최근 출근 일지 가져와서 퇴근시간이 null이 아니면 이미 출근 중인 상태)
        Optional<AttendanceLog> findLog = attendanceLogRepository.findByMember(projectId, memberId);
        AttendanceLogDto.SimpleResponse response = null;
        if (findLog.isPresent() && findLog.get().getEndAt() == null){
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
        }else {
            response = AttendanceLogDto.SimpleResponse.builder()
                    .isWoking(false)
                    .build();
        }

        return response;
    }
}
