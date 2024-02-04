package com.backend.prog.global.auth.dao;

import com.backend.prog.global.auth.domain.EmailAuth;
import org.springframework.data.repository.CrudRepository;

public interface EmailAuthRepository extends CrudRepository<EmailAuth, String> {
}
