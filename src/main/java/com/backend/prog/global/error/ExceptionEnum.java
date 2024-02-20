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
    ACCESS_DENIED_EXCEPTION(HttpStatus.FORBIDDEN, "E1003"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E1004"),
    UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, "E1005"),

    // Custom Exception CE1000번대
    //공통 에러: 1000번대
    DATA_DOES_NOT_EXIST(HttpStatus.BAD_REQUEST, "CE1003", Message.DATA_DOES_NOT_EXIST),
    NAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "CE1004", Message.NAME_ALREADY_EXISTS),

    //회원 관련 오류: 1100번
    INVALID_MEMBER_DATA(HttpStatus.BAD_REQUEST, "CE1100", Message.INVALID_MEMBER_DATA),
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "CE1101", Message.LOGIN_FAILED),
    ALREADY_EXIST_EMAIL(HttpStatus.CONFLICT, "CE1102", Message.ALREADY_EXIST_EMAIL),
    ALREADY_EXIST_NICKNAME(HttpStatus.CONFLICT, "CE1103", Message.ALREADY_EXIST_NICKNAME),
    PROHIBITED_NICKNAME(HttpStatus.BAD_REQUEST, "CE1104", Message.PROHIBITED_NICKNAME),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "CE1105", Message.MEMBER_NOT_FOUND),
    FAIL_AUTH_PASSWORD(HttpStatus.BAD_REQUEST, "CE1106", Message.FAIL_AUTH_PASSWORD),
    NOT_MATCH_CODE(HttpStatus.BAD_REQUEST, "CE1107", Message.NOT_MATCH_CODE),
    ALREADY_WITHDRAWAL_MEMBER(HttpStatus.BAD_REQUEST, "CE1108", Message.ALREADY_WITHDRAWAL_MEMBER),

    //토큰 관련 오류: 1200번
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "CE1200", Message.INVALID_ACCESS_TOKEN),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "CE1201", Message.EXPIRED_ACCESS_TOKEN),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "CE1202", Message.INVALID_REFRESH_TOKEN),
    EXPIRED_REFRESH_JWT(HttpStatus.UNAUTHORIZED, "CE1203", Message.EXPIRED_REFRESH_JWT),

    //프로젝트 관련 오류 : 1300번
    DATA_CANNOT_ADD(HttpStatus.BAD_REQUEST, "CE1300", Message.DATA_CANNOT_ADD),
    POSITION_FULL(HttpStatus.BAD_REQUEST, "CE1301", Message.POSITION_FULL),
    NOT_HAVE_POSITION(HttpStatus.BAD_REQUEST, "CE1302", Message.NOT_HAVE_POSITION),
    PROJECT_MEMBER(HttpStatus.BAD_REQUEST, "CE1303", Message.PROJECT_MEMBER),
    AUTHORITY_NOT_HAVE(HttpStatus.BAD_REQUEST, "CE1304", Message.AUTHORITY_NOT_HAVE),
    ALREADY_EXIST_Like(HttpStatus.CONFLICT, "CE1305", Message.ALREADY_EXIST_Like),
    ALREADY_EXIST_START(HttpStatus.CONFLICT, "CE1305", Message.ALREADY_EXIST_START),
    ALREADY_EXIST_END(HttpStatus.CONFLICT, "CE1306", Message.ALREADY_EXIST_END),
    ALREADY_EXIST_STARTAT(HttpStatus.BAD_REQUEST, "CE1307", Message.ALREADY_EXIST_STARTAT),
    ALREADY_EXIST_ENTAT(HttpStatus.BAD_REQUEST, "CE1308", Message.ALREADY_EXIST_ENTAT),
    ONLY_REGISTER_THREE(HttpStatus.BAD_REQUEST, "CE1309", Message.ONLY_REGISTER_THREE),
    
    //댓글 관련 오류 : 1400번
    INVALID_CONTENT_DATA(HttpStatus.BAD_REQUEST, "CE1400", Message.INVALID_CONTENT_DATA);

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

        String INVALID_MEMBER_DATA = "회원의 데이터가 유효하지 않습니다.";
        String LOGIN_FAILED = "아이디나 비밀번호를 확인해 주세요";
        String ALREADY_EXIST_EMAIL = "이미 존재하는 이메일입니다.";
        String ALREADY_EXIST_NICKNAME = "이미 존재하는 닉네임입니다.";
        String PROHIBITED_NICKNAME = "사용이 금지된 닉네임입니다.";
        String MEMBER_NOT_FOUND = "존재하지 않는 회원입니다.";
        String FAIL_AUTH_PASSWORD = "비밀번호가 틀렸습니다.";
        String NOT_MATCH_CODE = "인증번호가 일치하지 않습니다.";
        String ALREADY_WITHDRAWAL_MEMBER = "이미 탈퇴한 회원입니다.";

        String INVALID_ACCESS_TOKEN = "유효하지 않은 엑세스 토큰입니다.";
        String EXPIRED_ACCESS_TOKEN = "만료된 엑세스 토큰 입니다.";
        String INVALID_REFRESH_TOKEN = "유효하지 않은 리프레시 토큰입니다.";
        String EXPIRED_REFRESH_JWT = "만료된 리프레시 토큰입니다.";

        String DATA_CANNOT_ADD = "더이상 데이터를 추가할 수 없습니다.";
        String POSITION_FULL = "해당 포지션은 더이상 추가할 수 없습니다.";
        String NOT_HAVE_POSITION = "존재하지 않는 포지션입니다.";
        String PROJECT_MEMBER = "이미 신청했거나 참가중인 프로젝트입니다.";
        String AUTHORITY_NOT_HAVE = "권한이 없는 멤버입니다.";
        String ALREADY_EXIST_Like = "이미 좋아요를 누른 멤버입니다.";
        String ALREADY_EXIST_START = "이미 시작된 프로젝트 입니다.";
        String ALREADY_EXIST_END = "이미 종료된 프로젝트 입니다.";
        String ALREADY_EXIST_STARTAT = "이미 출근 상태입니다.";
        String ALREADY_EXIST_ENTAT = "퇴근을 하려면 출근 상태여야 합니다.";
        String ONLY_REGISTER_THREE = "3개 이하의 액션만 등록할 수 있습니다.";
        String INVALID_CONTENT_DATA = "존재하지 않는 컨텐츠 타입입니다.";
    }
}