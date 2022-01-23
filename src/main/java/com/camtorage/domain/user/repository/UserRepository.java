package com.camtorage.domain.user.repository;

import com.camtorage.entity.user.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>, UserRepositoryCustom {

    public Optional<User> findUserByEmail(String email);

    public Optional<User> findUserByIdAndIsPublic(Integer id, Boolean isPublic);

}
