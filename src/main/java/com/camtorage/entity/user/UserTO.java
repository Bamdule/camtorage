package com.camtorage.entity.user;

import com.camtorage.entity.friend.FriendStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTO {
    private Integer id;

    private String name;

    private String profileUrl;

    @JsonIgnore
    private String profilePath;

    private FriendStatus status;

}
