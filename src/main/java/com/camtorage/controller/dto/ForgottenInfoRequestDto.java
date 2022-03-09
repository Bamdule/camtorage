package com.camtorage.controller.dto;

import javax.validation.constraints.NotBlank;

import com.camtorage.db.user.UserCommand;
import com.camtorage.db.user.forgotteninfo.ForgottenInfoCommand;

import lombok.Getter;
import lombok.Setter;

public class ForgottenInfoRequestDto {
    @Getter
    @Setter
    public static class UpdatePassword {

        @NotBlank(message = "이메일은 필수 값입니다.")
        private String email;

        @NotBlank(message = "비밀번호는 필수 값입니다.")
        private String password;

        @NotBlank(message = "인증 코드는 필수 값입니다.")
        private String code;

        public ForgottenInfoCommand.UpdatePassword toCommand() {
            return ForgottenInfoCommand.UpdatePassword
                .builder()
                .email(email)
                .password(password)
                .code(code)
                .build();
        }
    }
}
