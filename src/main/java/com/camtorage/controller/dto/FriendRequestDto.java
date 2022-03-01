package com.camtorage.controller.dto;

import lombok.Getter;
import lombok.Setter;

public class FriendRequestDto {

    @Getter
    @Setter
    public static class SearchRequest {
        private String searchText;
    }
}
