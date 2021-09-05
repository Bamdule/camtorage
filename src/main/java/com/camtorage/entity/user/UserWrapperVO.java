package com.camtorage.entity.user;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserWrapperVO {

    private String code = "ok";
    private UserVO user;

    private long followerCnt = 0;
    private long followingCnt = 0;
    private long gearCnt = 0;
    private long boardCnt = 0;


}
