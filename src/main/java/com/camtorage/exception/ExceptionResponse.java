package com.camtorage.exception;

import org.springframework.validation.Errors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class ExceptionResponse {

    private ExceptionResponse() {
    }

    public static Map<String, Object> error(String error, String message) {
        Map<String, Object> errorMap = new IdentityHashMap<>();

        errorMap.put("timestamp", LocalDateTime.now());
        errorMap.put("error", error);

        if(message!= null) {
            errorMap.put("message", message);
        }

        return errorMap;
    }

    public static Map<String, Object> fieldErrors(String error, Errors errors) {


        List<Map<String, Object>> fieldErrors = new ArrayList<>();

        errors.getFieldErrors().forEach(e -> {
            Map<String, Object> fieldError = new IdentityHashMap<>();

            fieldError.put("field", e.getField());
            fieldError.put("code", e.getCode());
            fieldError.put("message", e.getDefaultMessage());
            if (e.getRejectedValue() != null) {
                fieldError.put("rejectedValue", e.getRejectedValue());
            }
            fieldErrors.add(fieldError);
        });

        Map<String, Object> errorMap = error(error, null);
        errorMap.put("errors", fieldErrors);

        return errorMap;
    }

}
