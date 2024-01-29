package com.backend.prog.domain.manager.dao;

import com.backend.prog.domain.manager.domain.Code;
import com.backend.prog.domain.manager.domain.CodeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeDetailRepository extends JpaRepository<CodeDetail, Integer> {

    List<CodeDetail> findByCode(Code code);

    CodeDetail findByCodeAndDetailName(Code code, String detailName);

}
