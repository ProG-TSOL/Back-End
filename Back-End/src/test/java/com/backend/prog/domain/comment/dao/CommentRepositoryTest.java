package com.backend.prog.domain.comment.dao;

import com.backend.prog.domain.attendance.dao.AttendanceLogRepository;
import com.backend.prog.domain.attendance.dao.AttendanceRepository;
import com.backend.prog.domain.attendance.domain.Attendance;
import com.backend.prog.domain.attendance.domain.AttendanceLog;
import com.backend.prog.domain.comment.domain.Comment;
import com.backend.prog.domain.comment.dto.CommentSimpleDto;
import com.backend.prog.domain.manager.dao.CodeDetailRepository;
import com.backend.prog.domain.manager.dao.CodeRepository;
import com.backend.prog.domain.manager.domain.Code;
import com.backend.prog.domain.manager.domain.CodeDetail;
import com.backend.prog.domain.member.dao.MemberRepository;
import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.domain.project.dao.ProjectRespository;
import com.backend.prog.domain.project.domain.Project;
import com.backend.prog.global.common.constant.CommonCode;
import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;


    @Autowired
    private CodeDetailRepository codeDetailRepository;

    @Autowired
    private CodeRepository codeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private AttendanceLogRepository attendanceLogRepository;

    @Autowired
    private ProjectRespository projectRespository;

    @Test
    public void 댓글() throws Exception {
        Code code = codeRepository.findByName(CommonCode.CODE_CONTENT);
        CodeDetail codeDetail = codeDetailRepository.findByCodeAndDetailName(code, CommonCode.CONTENT_WORK);

        Long workId = 1L;

        List<CommentSimpleDto> comments = commentRepository.getComments(codeDetail, workId);

        comments.forEach(comment -> {
            System.out.println("comment : " + comment);
        });

        org.assertj.core.api.Assertions.assertThat(comments.size()).isEqualTo(0);
    }

    @Test
    public void 출퇴근() throws Exception {
        Member member = memberRepository.findById(1).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        Project project = projectRespository.findById(1L).orElseThrow(() -> new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST));

        AttendanceLog attendanceLog = AttendanceLog.builder()
                .member(member)
                .project(project)
                .startAt(LocalDateTime.now())
                .build();

        attendanceLogRepository.save(attendanceLog);

        Thread.sleep(10);

        LocalDateTime endTime = LocalDateTime.now();
        Duration duration = Duration.between(attendanceLog.getStartAt(), endTime);
        Long workTime = duration.toHours();
        System.out.println("퇴근 시간 " + attendanceLog.getStartAt() +  " - 출근시간" + endTime + " = " + duration);
        System.out.println("시간ㄴㄴㄴㄴ" + workTime);

        if (workTime > 12) {
            endTime = attendanceLog.getStartAt().plusHours(12);
        }

        Optional<Attendance> findAttendance = attendanceRepository.findByDay(attendanceLog.getProject(), attendanceLog.getMember(), attendanceLog.getStartAt().toLocalDate());

        if (attendanceLog.getStartAt().toLocalDate().equals(endTime.toLocalDate())) {
            attendanceLog.addEndAt(endTime);
            Duration workTimes = Duration.between(attendanceLog.getStartAt().toLocalTime(), attendanceLog.getEndAt().toLocalTime());
            if (findAttendance.isPresent()) {
                Attendance attendance = findAttendance.get();
                System.out.println("1번");
                System.out.println("1번" + workTimes.getSeconds());
                System.out.println("퇴근전 : " + attendance.getWorkingTime());
                attendance.plusWorkingTime(workTimes.getSeconds());
                System.out.println("퇴근후 : " + attendance.getWorkingTime());
            } else {
                Attendance attendance = Attendance.builder()
                        .project(attendanceLog.getProject())
                        .member(attendanceLog.getMember())
                        .workingDay(attendanceLog.getStartAt().toLocalDate())
                        .workingTime(LocalTime.ofNanoOfDay(workTimes.toNanos()))
                        .build();

                attendanceRepository.save(attendance);
            }
        }
        //같지 않으면 end값 start 날짜의
        else {
            //출근 로그 퇴근시간을 해당일 23:59:59
            attendanceLog.addEndAt(attendanceLog.getStartAt().withHour(23).withMinute(59).withSecond(59));
            Duration workTimeStart = Duration.between(attendanceLog.getStartAt().toLocalTime(), attendanceLog.getEndAt().toLocalTime());
            // 출근일 기준 근태 추가
            if (findAttendance.isPresent()) {
                Attendance attendance = findAttendance.get();
                System.out.println("2번");
                System.out.println("2번" + workTimeStart.getNano());
                System.out.println("퇴근전 : " + attendance.getWorkingTime());
                attendance.plusWorkingTime(workTimeStart.getSeconds());
                System.out.println("퇴근전 : " + attendance.getWorkingTime());
            } else {
                Attendance attendanceStart = Attendance.builder()
                        .project(attendanceLog.getProject())
                        .member(attendanceLog.getMember())
                        .workingDay(attendanceLog.getStartAt().toLocalDate())
                        .workingTime(LocalTime.ofNanoOfDay(workTimeStart.toNanos()))
                        .build();

                attendanceRepository.save(attendanceStart);
            }

            //퇴근일 기준 새로운 로그 생성 출근시간 00:00:00 퇴근시간 endTime
            AttendanceLog attendanceLog2 = AttendanceLog.builder()
                    .member(attendanceLog.getMember())
                    .project(attendanceLog.getProject())
                    .startAt(endTime.withHour(0).withMinute(0).withSecond(0))
                    .endAt(endTime)
                    .build();
            Duration workTimeEnd = Duration.between(attendanceLog2.getStartAt().toLocalTime(), attendanceLog2.getEndAt().toLocalTime());
            //퇴근일 근태 생성
            Attendance attendancEnd = Attendance.builder()
                    .project(attendanceLog.getProject())
                    .member(attendanceLog.getMember())
                    .workingDay(attendanceLog.getStartAt().toLocalDate())
                    .workingTime(LocalTime.ofNanoOfDay(workTimeEnd.toNanos()))
                    .build();

            attendanceRepository.save(attendancEnd);
            attendanceLogRepository.save(attendanceLog);
        }
    }
}