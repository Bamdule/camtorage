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
    public List<UserResponse> users = new ArrayList<>();

    public long total;
    public int page;
    public int size;


}
