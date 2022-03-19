package com.camtorage.entity.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UserCommand {

    @Getter
    public static class Create {

        @Builder
        public Create(String email, String password, String name, String phone) {
            this.email = email;
            this.password = password;
            this.name = name;
            this.isPublic = true;
        }

        private String email;

        private String password;

        private String name;

        private Boolean isPublic;

    }

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
