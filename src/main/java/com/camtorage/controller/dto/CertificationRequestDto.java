package com.camtorage.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.camtorage.db.certification.email.EmailCertificationCommand;
import com.camtorage.db.certification.email.EmailCertificationType;

import lombok.Getter;
import lombok.Setter;

public class CertificationRequestDto {

    @Getter
    @Setter
    public static class EmailCertification {

        @NotBlank(message = "이메일은 필수 값입니다.")
        private String email;

        @NotNull(message = "인증 유형은 필수 값입니다.")
        private EmailCertificationType emailCertificationType;

        public EmailCertificationCommand.CreateEmailCertification toCommand() {

            return EmailCertificationCommand.CreateEmailCertification
                .builder()
                .email(email)
                .emailCertificationType(emailCertificationType)
                .build();
        }
    }

    @Getter
    @Setter
    public static class EmailCertificationValidation {
        @NotBlank(message = "인증코드는 필수 값입니다.")
        private String code;

        @NotBlank(message = "이메일은 필수 값입니다.")
        private String email;

        @NotNull(message = "인증 유형은 필수 값입니다.")
        private EmailCertificationType emailCertificationType;

        public EmailCertificationCommand.VerifyEmailCertification toCommand() {
            return EmailCertificationCommand.VerifyEmailCertification
                .builder()
                .code(code)
                .email(email)
                .emailCertificationType(emailCertificationType)
                .build();
        }
    }

}
