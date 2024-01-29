package com.backend.prog.domain.member.dao;

import com.backend.prog.domain.member.domain.MemberTech;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTechRepository extends JpaRepository<MemberTech, Integer> {
}
