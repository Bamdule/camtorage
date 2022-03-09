package com.camtorage.controller.dto;

import com.camtorage.db.user.service.UserCommand;

import lombok.Getter;
import lombok.Setter;

public class UserRequestDto {

    @Getter
    @Setter
    public static class PasswordCertification {
        private String password;

        public UserCommand.PasswordCertification toCommand(Integer userId) {
            return UserCommand.PasswordCertification
                .builder()
                .userId(userId)
                .password(this.password)
                .build();
        }
    }
}
