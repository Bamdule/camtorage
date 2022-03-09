package com.camtorage.db.user.forgotteninfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.camtorage.db.certification.email.EmailCertificationCommand;
import com.camtorage.db.certification.email.EmailCertificationService;
import com.camtorage.db.certification.email.EmailCertificationType;
import com.camtorage.domain.user.repository.UserRepository;
import com.camtorage.entity.user.User;
import com.camtorage.exception.CustomException;
import com.camtorage.exception.ExceptionCode;

@Service
public class ForgottenInfoServiceImpl implements ForgottenInfoService {

    @Autowired
    private EmailCertificationService emailCertificationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void updatePassword(ForgottenInfoCommand.UpdatePassword command) {

        final String email = command.getEmail();
        final String code = command.getCode();
        final String password = command.getPassword();

        EmailCertificationCommand.VerifyEmailCertification verifyEmailCertification
            = EmailCertificationCommand.VerifyEmailCertification
            .builder()
            .email(email)
            .code(code)
            .emailCertificationType(EmailCertificationType.FIND_PASSWORD)
            .build();

        emailCertificationService.verifyEmailCertification(verifyEmailCertification);

        User user = userRepository.findUserByEmail(email)
            .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_EXISTED));

        user.setPassword(passwordEncoder.encode(password));
    }
}
