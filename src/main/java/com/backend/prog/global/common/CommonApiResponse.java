package com.backend.prog.global.common;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
/*
  @param status 응답 상태
 * @param cnt 응답 데이터 개수
 * @param <T> data 응답 데이터
 */
public class CommonApiResponse<T> {
    private final LocalDateTime responseTime = LocalDateTime.now();
    private final HttpStatus status;
    private final int cnt;
    private final T data;

    @Builder
    private CommonApiResponse(HttpStatus status, T data, int cnt) {
        this.status = status;
        this.data = data;
        this.cnt = cnt;
    }
}
