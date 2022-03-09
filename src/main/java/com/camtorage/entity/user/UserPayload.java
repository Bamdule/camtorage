package com.camtorage.entity.user;

import com.camtorage.aop.LoginUser;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.lang.annotation.Annotation;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPayload implements LoginUser {
    @ApiModelProperty(hidden = true)
    private Integer userId;

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
