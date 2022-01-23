package com.camtorage.controller.email;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.camtorage.exception.CustomException;
import com.camtorage.exception.ExceptionCode;

@RestController
@RequestMapping(value = "/api/email")
public class EmailController {

    private final JavaMailSender mailSender;

    private Map<String, EmailCertification> certification = new HashMap<>();
    private final int codeLength = 8;

    public EmailController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @PostMapping(value = "/send-certification-code", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> sendAuthCodeToEmail(
        @RequestParam(value = "email") String email
    ) {

        EmailCertification emailCertification = EmailCertification.builder()
            .code(RandomStringUtils.randomAlphanumeric(codeLength))
            .email(email)
            .build();

        certification.put(email, emailCertification);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Camtorage 인증 코드가 도착했습니다.");
        message.setText(String.format("인증 코드는 [%s]입니다.", emailCertification.getCode()));

        mailSender.send(message);

        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/check-certification-code", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> checkAuthCode(
        @RequestParam(value = "email") String email,
        @RequestParam(value = "code") String code
    ) {

        EmailCertification emailCertification = certification.get(email);

        if (Objects.isNull(emailCertification)) {
            throw new CustomException(ExceptionCode.EMAIL_CERTIFICATION_NOT_FOUND);

        } else if (emailCertification.isExpired()) {
            certification.remove(email);
            throw new CustomException(ExceptionCode.EMAIL_CERTIFICATION_EXPIRED);

        } else if (!emailCertification.isValidCode(email, code)) {
            throw new CustomException(ExceptionCode.EMAIL_CERTIFICATION_INVALID);
        }

        certification.remove(email);

        return ResponseEntity.noContent().build();
    }
}
