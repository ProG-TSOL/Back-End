package com.backend.prog.global.auth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash(value = "refreshToken")
@ToString
public class RefreshToken {
    @Id
    private Integer id;

    private String refreshToken;

    @TimeToLive
    private Integer expiration;

    @Builder
    private RefreshToken(Integer id, String refreshToken, Integer expiration) {
        this.id = id;
        this.refreshToken = refreshToken;
        this.expiration = expiration;
    }
}