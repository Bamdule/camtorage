package com.camtorage.entity.user;

import com.camtorage.domain.user.dto.UserResponseDto;
import com.camtorage.entity.friend.FriendRelationship;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserWrapperVO {

    private UserResponseDto user;

    private long followerCnt = 0;
    private long followingCnt = 0;
    private long gearCnt = 0;
    private long boardCnt = 0;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FriendRelationship status = null;

}
