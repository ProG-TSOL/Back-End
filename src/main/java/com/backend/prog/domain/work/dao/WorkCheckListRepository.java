package com.backend.prog.domain.work.dao;

import com.backend.prog.domain.work.domain.Work;
import com.backend.prog.domain.work.domain.WorkCheckList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface WorkCheckListRepository extends JpaRepository<WorkCheckList, Long> {

    List<WorkCheckList> findAllByWork(Work work);
}
