package com.camtorage.db.user.forgotteninfo;

import lombok.Builder;
import lombok.Getter;

public class ForgottenInfoCommand {

    @Getter
    public static class UpdatePassword {

        private String email;
        private String password;
        private String code;

        @Builder
        public UpdatePassword(String email, String password, String code) {
            this.email = email;
            this.password = password;
            this.code = code;
        }
    }
}
