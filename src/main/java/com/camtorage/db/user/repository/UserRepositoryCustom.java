package com.camtorage.db.user.repository;

import com.camtorage.entity.user.UserSearch;
import com.camtorage.entity.friend.FriendVO;
import com.camtorage.entity.user.UserResponse;
import com.camtorage.entity.user.UserTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryCustom {

    public Optional<UserResponse> getUserById(Integer id);

    public List<UserTO> searchUser(UserSearch userSearch, Pageable pageable);

}
