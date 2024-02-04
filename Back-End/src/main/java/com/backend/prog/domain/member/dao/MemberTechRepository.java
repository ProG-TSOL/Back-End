package com.backend.prog.domain.member.dao;

import com.backend.prog.domain.member.domain.MemberTech;
import com.backend.prog.domain.member.domain.MemberTechId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberTechRepository extends JpaRepository<MemberTech, MemberTechId> {
}