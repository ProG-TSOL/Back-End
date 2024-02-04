package com.backend.prog.global.auth.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@RedisHash(value = "blacklist")
public class Blacklist {

    @Id
    private Integer id;

    private String accessToken;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Integer expiration;

    @Builder
    private Blacklist(Integer id, String accessToken, Integer expiration) {
        this.id = id;
        this.accessToken = accessToken;
        this.expiration = expiration;
    }
}