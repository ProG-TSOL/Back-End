package com.backend.prog.domain.attendance.dao;

import com.backend.prog.domain.attendance.domain.AttendanceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Long> {
    @Query(value = "select al from AttendanceLog al " +
            "where al.project.id = :projectId " +
            "and al.member.id = :memberId " +
            "order by al.createdAt desc " +
            "limit 1")
    Optional<AttendanceLog> findByMember(@Param("projectId") Long projectId, @Param("memberId") Integer memberId);

    @Query(value = "select al from AttendanceLog al " +
            "where al.project.id = :projectId " +
            "and al.member.id = :memberId " +
            "and year(al.startAt) = :year " +
            "and month (al.startAt) = :month " +
            "and day (al.startAt) = :day")
    List<AttendanceLog> findAllByLocalDate(@Param("projectId") Long projectId, @Param("memberId") Integer memberId,
                                           @Param("year") Integer year, @Param("month") Integer month, @Param("day") Integer day);
}
