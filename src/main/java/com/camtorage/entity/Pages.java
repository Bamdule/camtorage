package com.camtorage.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Pages<T> {
    private List<T> contents;
    private int page;
    private int size;
    private Long total;

    public static Pages empty() {
        return Pages.builder()
            .contents(new ArrayList<>())
            .page(0)
            .size(0)
            .total(0L)
            .build();
    }
}
