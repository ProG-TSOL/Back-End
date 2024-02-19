package com.backend.prog.global.error;

import lombok.Getter;

/**
 * 공통 에러 처리를 위한 Exception
 * ExceptionEnum을 통해 에러 코드와 메시지를 정의
 */
@Getter
public class CommonException extends RuntimeException {
    private final ExceptionEnum error;

    public CommonException(ExceptionEnum e) {
        super(e.getMessage());
        this.error = e;
    }
}
