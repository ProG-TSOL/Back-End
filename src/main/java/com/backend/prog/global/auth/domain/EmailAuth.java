package com.backend.prog.global.auth.domain;


import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@RedisHash(value = "emailAuth")
public class EmailAuth {

    @Id
    private String id;

    private String authCode;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Integer expiration;

    @Builder
    private EmailAuth(String id, String authCode, Integer expiration) {
        this.id = id;
        this.authCode = authCode;
        this.expiration = expiration;
    }
}
