package com.camtorage.controller.email;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EmailCertification {
    final int validMinute = 3;

    @Builder
    public EmailCertification(String code, String email) {
        this.code = code;
        this.email = email;
        this.expiryTime = LocalDateTime.now().plusMinutes(validMinute);
    }

    private String code;
    private String email;
    private LocalDateTime expiryTime;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryTime);
    }

    public boolean isValidCode(String email, String code) {
        return this.code.equals(code) && this.email.equals(email);
    }
}
