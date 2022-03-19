package com.camtorage.controller.dto;

import javax.validation.constraints.NotBlank;

import com.camtorage.entity.user.UserCommand;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRequestDto {

    @Getter
    @Setter
    public static class CreatePasswordCertification {
        private String password;

        public UserCommand.VerifyPasswordCertification toCommand(Integer userId) {
            return UserCommand.VerifyPasswordCertification
                .builder()
                .userId(userId)
                .password(this.password)
                .build();
        }
    }

    @Getter
    @Setter
    public static class CreateRequest {
        @NotBlank(message = "이메일은 필수 값입니다.")
        private String email;

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        @NotBlank(message = "비밀번호는 필수 값입니다.")
        private String password;

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private String name;

        public UserCommand.Create toCommand() {
            return UserCommand.Create
                .builder()
                .email(this.email)
                .password(this.password)
                .name(this.name)
                .build();
        }
    }
}
