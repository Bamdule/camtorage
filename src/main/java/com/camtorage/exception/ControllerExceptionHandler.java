package com.camtorage.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * @author kim RestController에서 발생하는 에러를 핸들링하는 클래스
 */
@RestControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    //@Valid 검증 실패 시 Catch

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<Map<String, Object>> bindException(BindException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        Map<String, Object> errors = ExceptionResponse.fieldErrors(
                "Bad Request",
                e.getBindingResult()
        );

        return ResponseEntity.status(httpStatus).body(errors);
    }

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<Map<String, Object>> customException(CustomException e) {

        if (e.getException() != null) {
            logger.error("", e.getException());
        }

        HttpStatus httpStatus = e.getExceptionCode().getHttpStatus();
        Map<String, Object> errors = ExceptionResponse.error(
                e.getExceptionCode().getCode(),
                e.getMessage() == null ? e.getExceptionCode().getMessage() : e.getMessage()
        );

        return ResponseEntity.status(httpStatus).body(errors);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Map<String, Object>> exception(Exception e) {

        logger.error("[SERVER-ERROR]", e);

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        Map<String, Object> errors = ExceptionResponse.error(
                "INTERNAL_SERVER_ERROR",
                e.toString()
        );
        logger.error("[SERVER-ERROR] {}", errors);

        return ResponseEntity.status(httpStatus).body(errors);
    }

}
