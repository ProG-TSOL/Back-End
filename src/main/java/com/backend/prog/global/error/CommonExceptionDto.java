package com.backend.prog.global.error;

import lombok.Builder;

/**
 * 공통 에러 처리를 위한 DTO
 * @param errorCode
 * @param errorMessage
 */
@Builder
public record CommonExceptionDto(String errorCode, String errorMessage) {
}
