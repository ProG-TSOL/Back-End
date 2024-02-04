package com.backend.prog.domain.retrospect.dao;

import com.backend.prog.domain.retrospect.dao.custom.RetrospectRepositoryCustom;
import com.backend.prog.domain.retrospect.domain.Retrospect;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetrospectRepository extends JpaRepository<Retrospect, Long>, RetrospectRepositoryCustom {
}
