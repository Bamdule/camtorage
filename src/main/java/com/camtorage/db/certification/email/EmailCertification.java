package com.camtorage.db.certification.email;

import java.time.LocalDateTime;
import java.util.Objects;

import org.apache.commons.lang3.RandomStringUtils;

import com.camtorage.exception.CustomException;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EmailCertification {

    @Builder
    public EmailCertification(String email, EmailCertificationType emailCertificationType) {

        this.emailCertificationType = emailCertificationType;
        this.email = email;
        this.code = this.createCertificationCode();
        this.expiryTime = this.createExpiryTime();
    }

    private String code;
    private String email;
    private LocalDateTime expiryTime;
    private EmailCertificationType emailCertificationType;

    private String createCertificationCode() {
        return RandomStringUtils.randomAlphanumeric(emailCertificationType.getCertificationCodeLength());
    }

    private LocalDateTime createExpiryTime() {
        return LocalDateTime.now().plusMinutes(emailCertificationType.getValidMinutes());
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryTime);
    }

    public boolean isValidCode(String email, String code) {
        return this.code.equals(code) && this.email.equals(email);
    }

    public boolean equalValidCertificationType(EmailCertificationType emailCertificationType) {
        if (Objects.isNull(emailCertificationType)) {
            return false;
        }

        return this.emailCertificationType.equals(emailCertificationType);
    }
}
