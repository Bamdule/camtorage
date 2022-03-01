package com.camtorage.db.friend.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.camtorage.controller.dto.FriendRequestDto;
import com.camtorage.domain.user.dto.search.UserSearchCondition;
import com.camtorage.entity.Pages;
import com.camtorage.entity.friend.FriendRelationship;
import com.camtorage.entity.friend.FriendVO;

public interface FriendService {

    public void requestFriend(Integer userId, Integer friendId);

    public void acceptFriend(Integer userId, Integer friendId);

    public void deleteFriend(Integer userId, Integer friendId);

    Pages<FriendVO> findFollowersByUserId(
        Integer userId,
        FriendRequestDto.SearchRequest searchRequest,
        Pageable pageable
    );

    Pages<FriendVO> findFollowingsByUserId(
        Integer userId,
        FriendRequestDto.SearchRequest searchRequest,
        Pageable pageable
    );

    public boolean isFriend(Integer userId, Integer friendId);

    public long getCountFollower(Integer userId);

    public long getCountFollowing(Integer userId);

    public List<FriendVO> searchFriend(UserSearchCondition userSearchCondition, Pageable pageable);

    public FriendRelationship getFriendRelationship(Integer myUserId, Integer otherUserId);

    void deleteAll(Integer userId);
}
