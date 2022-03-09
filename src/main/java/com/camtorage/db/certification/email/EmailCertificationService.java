package com.camtorage.db.certification.email;

public interface EmailCertificationService {

    EmailCertificationInfo.Main createEmailCertification(EmailCertificationCommand.CreateEmailCertification command);

    void verifyEmailCertification(EmailCertificationCommand.VerifyEmailCertification command);

}
