package com.camtorage.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserResponse {
    private Integer id;

    private String name = "";

    private String email = "";

    private String phone = "";

    private Integer userImageId = null;

    private String userImageUrl = "";

    private Boolean isPublic;

    private String aboutMe = "";

}
