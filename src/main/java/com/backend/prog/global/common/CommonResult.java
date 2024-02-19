package com.backend.prog.global.common;

import com.backend.prog.global.error.CommonExceptionDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Get 요청을 제외한 Rest API의 응답 객체
 */
@Getter
@NoArgsConstructor
@ToString
public class CommonResult {
    private String status;
    private String message;
    private CommonExceptionDto exceptionDto;

    @Builder
    public CommonResult(String status, String message, CommonExceptionDto exceptionDto) {
        this.status = status;
        this.message = message;
        this.exceptionDto = exceptionDto;
    }
}
