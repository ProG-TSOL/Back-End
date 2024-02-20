package com.backend.prog.global.error;

import com.backend.prog.global.common.CommonResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionAdvice {

    @ExceptionHandler({CommonException.class})
    public ResponseEntity<CommonResult> exceptionHandler(HttpServletRequest request, final CommonException e) {
        CommonExceptionDto dto = CommonExceptionDto.builder()
                .errorCode(e.getError().getCode())
                .errorMessage(e.getError().getMessage())
                .build();

        return ResponseEntity
                .status(e.getError().getStatus())
                .body(CommonResult.builder()
                        .status("FAIL")
                        .message(e.getError().getMessage())
                        .exceptionDto(dto)
                        .build());
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<CommonResult> exceptionHandler(HttpServletRequest request, final RuntimeException e) {
        CommonExceptionDto dto = CommonExceptionDto.builder()
                .errorCode(ExceptionEnum.RUNTIME_EXCEPTION.getCode())
                .errorMessage(e.getMessage())
                .build();

        return ResponseEntity
                .status(ExceptionEnum.RUNTIME_EXCEPTION.getStatus())
                .body(CommonResult.builder()
                        .status("FAIL")
                        .message(e.getMessage())
                        .exceptionDto(dto)
                        .build());
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<CommonResult> exceptionHandler(HttpServletRequest request, final AccessDeniedException e) {
        CommonExceptionDto dto = CommonExceptionDto.builder()
                .errorCode(ExceptionEnum.ACCESS_DENIED_EXCEPTION.getCode())
                .errorMessage(e.getMessage())
                .build();

        return ResponseEntity
                .status(ExceptionEnum.ACCESS_DENIED_EXCEPTION.getStatus())
                .body(CommonResult.builder()
                        .status("FAIL")
                        .message(e.getMessage())
                        .exceptionDto(dto)
                        .build());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<CommonResult> exceptionHandler(HttpServletRequest request, final Exception e) {
        CommonExceptionDto dto = CommonExceptionDto.builder()
                .errorCode(ExceptionEnum.INTERNAL_SERVER_ERROR.getCode())
                .errorMessage(e.getMessage())
                .build();

        return ResponseEntity
                .status(ExceptionEnum.INTERNAL_SERVER_ERROR.getStatus())
                .body(CommonResult.builder()
                        .status("FAIL")
                        .message(e.getMessage())
                        .exceptionDto(dto)
                        .build());
    }
}
