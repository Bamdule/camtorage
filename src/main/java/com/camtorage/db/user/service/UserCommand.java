package com.camtorage.db.user.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UserCommand {

    @Setter
    @Getter
    public static class PasswordCertification {

        @Builder
        public PasswordCertification(Integer userId, String password) {
            this.userId = userId;
            this.password = password;
        }

        private Integer userId;
        private String password;
    }
}
