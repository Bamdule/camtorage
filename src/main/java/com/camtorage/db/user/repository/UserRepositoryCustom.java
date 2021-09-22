package com.camtorage.db.user.repository;

import com.camtorage.entity.user.UserResponse;

import java.util.Optional;

public interface UserRepositoryCustom {

    public Optional<UserResponse> getUserById(Integer id);

}
