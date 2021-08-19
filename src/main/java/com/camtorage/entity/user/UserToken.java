package com.camtorage.entity.user;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserToken {

    private String token;

    private String email;
//    private String refreshJwt;
}
