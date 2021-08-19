package com.camtorage.jwt;

import com.camtorage.aop.LoginUser;
import lombok.*;

import java.lang.annotation.Annotation;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPayload implements LoginUser {
    private Integer userId;

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
