package com.camtorage.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.camtorage.controller.dto.CertificationRequestDto;
import com.camtorage.db.certification.email.EmailCertificationCommand;
import com.camtorage.db.certification.email.EmailCertificationInfo;
import com.camtorage.db.certification.email.EmailCertificationService;

@RestController
@RequestMapping(value = "/api/certification")
public class CertificationController {

    @Autowired
    private EmailCertificationService emailCertificationService;

    @PostMapping(value = "/email")
    public ResponseEntity<EmailCertificationInfo.Main> createEmailCertification(
        @Valid CertificationRequestDto.EmailCertification emailCertification) {

        EmailCertificationCommand.CreateEmailCertification command = emailCertification.toCommand();

        EmailCertificationInfo.Main certification = emailCertificationService.createEmailCertification(command);

        return ResponseEntity.ok(certification);
    }

    @PostMapping(value = "/email/validation")
    public ResponseEntity<Void> verifyCertificationValidation(
        @Valid CertificationRequestDto.EmailCertificationValidation emailCertificationValidation) {

        EmailCertificationCommand.VerifyEmailCertification command = emailCertificationValidation.toCommand();

        emailCertificationService.verifyEmailCertification(command);

        return ResponseEntity.noContent().build();
    }

}
