package com.backend.prog.global.error;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {
    private final ExceptionEnum error;

    public CommonException(ExceptionEnum e) {
        super(e.getMessage());
        this.error = e;
    }
}
