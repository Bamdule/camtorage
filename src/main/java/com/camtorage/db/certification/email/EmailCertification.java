package com.camtorage.db.certification.email;

import java.time.LocalDateTime;

import org.apache.commons.lang3.RandomStringUtils;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EmailCertification {

    @Builder
    public EmailCertification(String email, EmailCertificationType emailCertificationType) {
        this.code = this.createCertificationCode(emailCertificationType.getCertificationCodeLength());
        this.expiryTime = this.createExpiryTime(emailCertificationType.getValidMinutes());
        this.email = email;
    }

    private String code;
    private String email;
    private LocalDateTime expiryTime;

    private String createCertificationCode(Integer certificationCodeLength) {
        return RandomStringUtils.randomAlphanumeric(certificationCodeLength);
    }

    private LocalDateTime createExpiryTime(Integer validMinutes) {
        return LocalDateTime.now().plusMinutes(validMinutes);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryTime);
    }

    public boolean isValidCode(String email, String code) {
        return this.code.equals(code) && this.email.equals(email);
    }
}
