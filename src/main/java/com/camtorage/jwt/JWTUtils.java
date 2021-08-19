package com.camtorage.jwt;

import com.camtorage.exception.CustomException;
import com.camtorage.exception.ExceptionCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.IdentityHashMap;
import java.util.Map;

public class JWTUtils {
    public JWTUtils() {
    }

    //토큰 생성
    protected String createToken(Map<String, Object> data, Long expiredTime, String key) {

        //Header 부분 설정
        Map<String, Object> headers = new IdentityHashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        //payload 부분 설정
        Map<String, Object> payloads = data;

        Date ext = new Date(); // 토큰 만료 시간
        ext.setTime(ext.getTime() + expiredTime);

        // 토큰 Builder
        String jwt = Jwts.builder()
                .setHeader(headers) // Headers 설정
                .setSubject("token")
                .setClaims(payloads) // Claims 설정
                .setExpiration(ext) // 토큰 만료 시간 설정
                .signWith(SignatureAlgorithm.HS256, key.getBytes()) // HS256과 Key로 Sign
                .compact(); // 토큰 생성

        return jwt;
    }

    //토큰 검증
    protected Map<String, Object> verifyJWT(String jwt, String key) {

        Map<String, Object> claimMap = null;
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key.getBytes("UTF-8")) // Set Key
                    .parseClaimsJws(jwt) // 파싱 및 검증, 실패 시 에러
                    .getBody();

            claimMap = claims;

        } catch (ExpiredJwtException e) { // 토큰이 만료되었을 경우
            throw new CustomException(ExceptionCode.JWT_EXPIRATION, e);
        } catch (Exception e) {
            throw new CustomException(ExceptionCode.JWT_INVALID, e);
        }

        return claimMap;
    }
}
