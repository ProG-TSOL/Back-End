package com.backend.prog.domain.member.dao;

import com.backend.prog.domain.member.domain.MemberTech;
import com.backend.prog.domain.member.domain.MemberTechId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberTechRepository extends JpaRepository<MemberTech, MemberTechId> {

    @Query("select mt from MemberTech  mt where mt.member.id = :id")
    List<MemberTech> findAllByOnlyId(Integer id);
}