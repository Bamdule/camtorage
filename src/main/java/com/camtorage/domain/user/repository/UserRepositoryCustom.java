package com.camtorage.domain.user.repository;

import com.camtorage.domain.user.dto.UserResponseDto;
import com.camtorage.domain.user.dto.search.UserSearchResponse;
import org.springframework.data.domain.Pageable;
import com.camtorage.domain.user.dto.search.UserSearchCondition;

import java.util.Optional;

public interface UserRepositoryCustom {

    public Optional<UserResponseDto> getUserById(Integer id);

    public UserSearchResponse searchUser(UserSearchCondition userSearchCondition, Pageable pageable);

}
