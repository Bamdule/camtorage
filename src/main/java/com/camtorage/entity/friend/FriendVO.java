package com.camtorage.entity.friend;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendVO {
    private Integer id;

    private Integer friendId;

    private String name = "";

    private String profileUrl = null;

    private String email = "";

    @JsonIgnore
    private String profilePath = "";

    private FriendStatus status;

}
