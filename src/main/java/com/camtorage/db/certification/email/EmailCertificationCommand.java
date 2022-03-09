package com.camtorage.db.certification.email;

import lombok.Builder;
import lombok.Getter;

public class EmailCertificationCommand {

    @Getter
    public static class CreateEmailCertification {

        @Builder
        public CreateEmailCertification(String email, EmailCertificationType emailCertificationType) {
            this.email = email;
            this.emailCertificationType = emailCertificationType;
        }

        private String email;
        private EmailCertificationType emailCertificationType;
    }

    @Getter
    public static class VerifyEmailCertification {
        private String email;
        private String code;
        private EmailCertificationType emailCertificationType;

        @Builder
        public VerifyEmailCertification(String email, String code, EmailCertificationType emailCertificationType) {
            this.email = email;
            this.code = code;
            this.emailCertificationType = emailCertificationType;
        }
    }
}
