package com.camtorage.entity.user;

import lombok.Builder;
import lombok.Getter;

public class UserInfo {

    @Getter
    public static class Create {

        private Integer id;

        private String email;

        private String name;

        private Boolean isPublic;

        @Builder
        public Create(Integer id, String email, String name, Boolean isPublic) {
            this.id = id;
            this.email = email;
            this.name = name;
            this.isPublic = isPublic;
        }

        public static UserInfo.Create of(User user) {
            return Create
                .builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .isPublic(user.getIsPublic())
                .build();
        }
    }
}
