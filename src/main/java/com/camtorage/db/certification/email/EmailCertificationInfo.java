package com.camtorage.db.certification.email;

import lombok.Builder;
import lombok.Getter;

public class EmailCertificationInfo {

    @Getter
    public static class Main {

        @Builder
        public Main(EmailCertificationType emailCertificationType) {
            this.emailCertificationType = emailCertificationType;
        }

        private EmailCertificationType emailCertificationType;
    }
}
