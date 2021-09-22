package com.camtorage.entity.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRequest {

    private Integer id;

    @NotBlank(message = "이메일은 필수 값입니다.")
    private String email;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @NotBlank(message = "비밀번호는 필수 값입니다.")
    private String password;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String name;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String phone;

    private Boolean isPublic;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String token;

}
