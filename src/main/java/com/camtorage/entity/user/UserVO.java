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
public class UserVO {
    private Integer id;

    private String name;

    private String email;

    private String phone;

    private Integer userImageId;

    private String userImageUrl;

    private String userImagePath;
}
