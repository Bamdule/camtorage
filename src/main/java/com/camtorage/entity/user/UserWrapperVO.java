package com.camtorage.entity.user;

import com.camtorage.domain.user.dto.UserResponse;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserWrapperVO {

    private UserResponse user;

    private long followerCnt = 0;
    private long followingCnt = 0;
    private long gearCnt = 0;
    private long boardCnt = 0;


}
