package com.camtorage.db.certification.email;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.camtorage.exception.CustomException;
import com.camtorage.exception.ExceptionCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmailCertificationServiceImpl implements EmailCertificationService {

    private Map<String, EmailCertification> certificationManager = new HashMap<>();
    private final JavaMailSender mailSender;

    @Override
    public EmailCertificationInfo.Main createEmailCertification(
        EmailCertificationCommand.CreateEmailCertification command) {

        final String email = command.getEmail();
        final EmailCertificationType emailCertificationType = command.getEmailCertificationType();

        EmailCertification emailCertification = EmailCertification.builder()
            .email(email)
            .emailCertificationType(emailCertificationType)
            .build();

        certificationManager.put(email, emailCertification);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Camtorage 인증 코드가 도착했습니다.");
        message.setText(String.format("인증 코드는 [%s]입니다.", emailCertification.getCode()));

        mailSender.send(message);

        return EmailCertificationInfo.Main
            .builder()
            .emailCertificationType(emailCertificationType)
            .build();
    }

    @Override
    public void verifyEmailCertification(EmailCertificationCommand.VerifyEmailCertification command) {

        final String email = command.getEmail();
        final String code = command.getCode();

        EmailCertification emailCertification = certificationManager.get(email);

        if (Objects.isNull(emailCertification)) {
            throw new CustomException(ExceptionCode.EMAIL_CERTIFICATION_NOT_FOUND);

        } else if (emailCertification.isExpired()) {
            certificationManager.remove(email);
            throw new CustomException(ExceptionCode.EMAIL_CERTIFICATION_EXPIRED);

        } else if (!emailCertification.isValidCode(email, code)) {
            throw new CustomException(ExceptionCode.EMAIL_CERTIFICATION_INVALID);
        } else if (!emailCertification.equalValidCertificationType(command.getEmailCertificationType())) {
            certificationManager.remove(email);
            throw new CustomException(ExceptionCode.EMAIL_CERTIFICATION_NOT_FOUND);
        }
    }

}
