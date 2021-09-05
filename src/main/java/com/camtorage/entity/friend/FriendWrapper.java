package com.camtorage.entity.friend;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendWrapper {
    private String description = "";
    private List<FriendVO> friends = new ArrayList<>();
}
