package com.backend.prog.global.auth.dao;

import com.backend.prog.global.auth.domain.BlackList;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BlackListRepository extends CrudRepository<BlackList, Integer> {

    Optional<BlackList> findByAccessToken(String accessToken);
}
