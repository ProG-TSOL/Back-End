package com.backend.prog.domain.attendance.dao;

import com.backend.prog.domain.attendance.domain.Attendance;
import com.backend.prog.domain.member.domain.Member;
import com.backend.prog.domain.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    @Query(value = "select a from Attendance a " +
            "where month(a.workingDay) = :month " +
            "and a.project.id = :projectId " +
            "and a.member.id = :memberId")
    List<Attendance> findAllByMonth(@Param("projectId") Long projectId,
                                    @Param("memberId") Integer memberId,
                                    @Param("month") Integer month);

    @Query(value = "select a from Attendance a " +
            "where a.workingDay = :startAt " +
            "and a.project = :project " +
            "and a.member = :members")
    Optional<Attendance> findByDay(@Param("project") Project project,
                                   @Param("members") Member member,
                                   @Param("startAt") LocalDate startAt);
}
