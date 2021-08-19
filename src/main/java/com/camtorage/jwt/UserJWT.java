package com.camtorage.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserJWT extends JWTUtils {

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtKey = "jwt_user_key";

    public String createJWT(UserPayload userPayload) {
        Long expiredTime = 1000 * 60L * 60L * 5L;
        Map map = objectMapper.convertValue(userPayload, Map.class);

        return super.createToken(map, expiredTime, jwtKey);
    }

    public UserPayload verifyJWT(String jwt) {
        Map<String, Object> map = super.verifyJWT(jwt, jwtKey);
        return objectMapper.convertValue(map, UserPayload.class);
    }

    public void onlyVerifyJWT(String jwt) {
        super.verifyJWT(jwt, jwtKey);
    }


}
