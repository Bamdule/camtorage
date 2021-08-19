package com.camtorage.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends RuntimeException {

    private ExceptionCode exceptionCode;

    private Exception exception;

    private String message;

    public CustomException(ExceptionCode exceptionCode) {
        this(exceptionCode, exceptionCode.getMessage());
    }

    public CustomException(ExceptionCode exceptionCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
        this.message = message;
    }

    public CustomException(ExceptionCode exceptionCode, Exception exception) {
        super(exception.getMessage());
        this.exceptionCode = exceptionCode;
        this.exception = exception;
    }
}
