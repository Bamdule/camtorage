package com.camtorage.db.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UserCommand {

    @Setter
    @Getter
    public static class VerifyPasswordCertification {

        @Builder
        public VerifyPasswordCertification(Integer userId, String password) {
            this.userId = userId;
            this.password = password;
        }

        private Integer userId;
        private String password;
    }
}
