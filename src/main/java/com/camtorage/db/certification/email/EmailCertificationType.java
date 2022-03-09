package com.camtorage.db.certification.email;

public enum EmailCertificationType {
    REGISTER(3, 8), FIND_PASSWORD(10, 8);

    private Integer validMinutes;
    private Integer certificationCodeLength;

    EmailCertificationType(Integer validMinutes, Integer certificationCodeLength) {
        this.validMinutes = validMinutes;
        this.certificationCodeLength = certificationCodeLength;
    }

    public Integer getValidMinutes() {
        return validMinutes;
    }

    public Integer getCertificationCodeLength() {
        return certificationCodeLength;
    }
}
