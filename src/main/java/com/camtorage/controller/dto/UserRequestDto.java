package com.camtorage.controller.dto;

import com.camtorage.db.user.UserCommand;

import lombok.Getter;
import lombok.Setter;

public class UserRequestDto {

    @Getter
    @Setter
    public static class PasswordCertification {
        private String password;

        public UserCommand.VerifyPasswordCertification toCommand(Integer userId) {
            return UserCommand.VerifyPasswordCertification
                .builder()
                .userId(userId)
                .password(this.password)
                .build();
        }
    }
}
