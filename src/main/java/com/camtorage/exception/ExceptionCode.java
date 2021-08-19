package com.camtorage.exception;

import org.springframework.http.HttpStatus;

/**
 * @author kim
 * <p>
 * 예외 코드를 관리하는 enum
 */
public enum ExceptionCode {

    USER_NOT_EXISTED(HttpStatus.BAD_REQUEST, "USER_NOT_EXISTED", "존재하지 않은 회원입니다."),
    FILE_NOT_EXISTED(HttpStatus.BAD_REQUEST, "FILE_NOT_EXISTED", "파일이 존재하지 않습니다."),
    NO_IMAGE(HttpStatus.BAD_REQUEST, "NO_IMAGE", "이미지 파일이 아닙니다."),

    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "LOGIN_FAILED", "아이디 또는 비밀번호가 틀렸습니다."),
    ALREADY_JOINED(HttpStatus.BAD_REQUEST, "ALREADY_JOINED", "이미 가입한 이메일 주소입니다."),
    JWT_EXPIRATION(HttpStatus.BAD_REQUEST, "JWT_EXPIRATION", "만료된 토큰입니다."),
    JWT_INVALID(HttpStatus.BAD_REQUEST, "JWT_INVALID", "올바르지 않은 토큰입니다."),


    AWS_S3_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AWS_S3_UPLOAD_ERROR", "AWS S3 이미지 파일 업로드 에러"),

    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    ExceptionCode(final HttpStatus httpStatus, final String code, final String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.code = code;
    }
}
