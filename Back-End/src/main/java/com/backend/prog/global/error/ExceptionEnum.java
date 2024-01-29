package com.backend.prog.global.error;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ExceptionEnum {
    /**
     * Http코드, Exception종류, 메세지
     */
    // System Exception E1000번대
    RUNTIME_EXCEPTION(HttpStatus.BAD_REQUEST, "E1000"),
    NULL_POINTER_EXCEPTION(HttpStatus.BAD_REQUEST, "E1001"),
    ILLEGAL_ARGUMENT_EXCEPTION(HttpStatus.BAD_REQUEST, "E1002"),
    ACCESS_DENIED_EXCEPTION(HttpStatus.BAD_REQUEST, "E1003"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E1004"),

    // Custom Exception CE1000번대
    //공통 에러: 1000번대
    DATA_DOES_NOT_EXIST(HttpStatus.BAD_REQUEST, "CE1003", Message.DATA_DOES_NOT_EXIST),
    NAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "CE1004", Message.NAME_ALREADY_EXISTS),

    //회원 관련 오류: 1100번
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "CE1100", Message.LOGIN_FAILED),

    //토큰 관련 오류: 1200번
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "CE1200", Message.INVALID_ACCESS_TOKEN),
    EXPIRED_ACCESS_JWT(HttpStatus.FORBIDDEN, "CE1201", Message.EXPIRED_ACCESS_JWT),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "CE1202", Message.INVALID_REFRESH_TOKEN),
    EXPIRED_REFRESH_JWT(HttpStatus.FORBIDDEN, "CE1203", Message.EXPIRED_REFRESH_JWT),

    //프로젝트 추가정보 관련 오류 : 1300번
    DATA_CANNOT_ADD(HttpStatus.BAD_REQUEST, "CE1300", Message.DATA_CANNOT_ADD);

    private final HttpStatus status;
    private final String code;
    private String message;

    ExceptionEnum(HttpStatus status, String code) {
        this.status = status;
        this.code = code;
    }

    ExceptionEnum(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public interface Message{
        String DATA_DOES_NOT_EXIST = "데이터가 존재하지 않습니다.";
        String NAME_ALREADY_EXISTS = "이미 존재하는 이름입니다.";

        String LOGIN_FAILED = "아이디나 비밀번호를 확인해 주세요";

        String EXPIRED_ACCESS_JWT = "만료된 토큰입니다.";
        String INVALID_ACCESS_TOKEN = "유효하지 않은 토큰입니다.";
        String EXPIRED_REFRESH_JWT = "만료된 토큰입니다.";
        String INVALID_REFRESH_TOKEN = "유효하지 않은 토큰입니다.";

        String DATA_CANNOT_ADD = "더이상 데이터를 추가할 수 없습니다.";
    }
}