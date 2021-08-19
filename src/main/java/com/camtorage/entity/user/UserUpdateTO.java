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
public class UserUpdateTO {
    private Integer id;

    private String password;

    private String name;

    private String phone;
}
