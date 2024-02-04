package com.backend.prog.global.auth.dao;

import com.backend.prog.global.auth.domain.Blacklist;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BlacklistRepository extends CrudRepository<Blacklist, Integer> {

    Optional<Blacklist> findByAccessToken(String accessToken);
}
