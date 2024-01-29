package com.backend.prog.global.error;

import lombok.Builder;

@Builder
public record CommonExceptionDto(String errorCode, String errorMessage) {
}
