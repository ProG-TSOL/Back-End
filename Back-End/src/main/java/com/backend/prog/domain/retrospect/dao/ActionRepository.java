package com.backend.prog.domain.retrospect.dao;

import com.backend.prog.domain.retrospect.dao.custom.ActionRepositoryCustom;
import com.backend.prog.domain.retrospect.domain.Action;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionRepository extends JpaRepository<Action, Long>, ActionRepositoryCustom {

}