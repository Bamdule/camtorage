package com.camtorage.domain.user.dto.search;

import com.camtorage.domain.user.dto.UserResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSearchResponse {
    private List<UserResponse> users = new ArrayList<>();

    private long total = 0;
    private int page = 0;
    private int size = 20;

}
