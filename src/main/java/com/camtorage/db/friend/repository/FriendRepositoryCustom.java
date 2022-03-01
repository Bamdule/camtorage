package com.camtorage.db.friend.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.camtorage.controller.dto.FriendRequestDto;
import com.camtorage.entity.Pages;
import com.camtorage.entity.friend.Friend;
import com.camtorage.entity.friend.FriendStatus;
import com.camtorage.entity.friend.FriendVO;

public interface FriendRepositoryCustom {
    public Optional<Friend> findFriendByUserIdAndFriendId(Integer userId, Integer friendId);

    public Optional<Friend> findFriendByUserIdAndFriendIdAndStatus(Integer userId, Integer friendId,
        FriendStatus status);

    Pages<FriendVO> findFollowersByUserId(Integer userId, FriendRequestDto.SearchRequest searchRequest,
        Pageable pageable);

    Pages<FriendVO> findFollowingsByUserId(Integer userId, FriendRequestDto.SearchRequest searchRequest,
        Pageable pageable);

    public long getCountFollower(Integer userId);

    public long getCountFollowing(Integer userId);

    void deleteAll(Integer id);

}
