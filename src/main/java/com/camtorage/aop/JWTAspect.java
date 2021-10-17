package com.camtorage.aop;

import com.camtorage.jwt.UserJWT;
import com.camtorage.entity.user.UserPayload;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class JWTAspect {
    @Autowired
    private UserJWT userJWT;

    /**
     * 메소드에 @UserJWTCheck 선언 시 아래 메소드를 거쳐감 
     * @param jp
     */
    @Before("@annotation(com.camtorage.aop.UserJWTCheck)")
    public void memberLoginCheck(JoinPoint jp) {
        System.out.println("[MYTEST] GOGO!!");
    }

    @Around("execution(* *(.., @LoginUser (*), ..))")
    public Object convertUser(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        String jwt = request.getHeader("authorization");

        //jwt 유효성 검사 실패 시 exception 발생
        UserPayload userPayload = userJWT.verifyJWT(jwt);

        Object[] args = Arrays.stream(joinPoint.getArgs()).map(data -> {
            if (data instanceof LoginUser) {
                data = userPayload;
            }
            return data;
        }).toArray();

        return joinPoint.proceed(args);
    }


}
