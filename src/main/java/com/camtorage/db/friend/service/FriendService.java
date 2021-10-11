package com.camtorage.db.friend.service;

import com.camtorage.domain.user.dto.search.UserSearchCondition;
import com.camtorage.entity.friend.FriendVO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FriendService {

    public void requestFriend(Integer userId, Integer friendId);

    public void acceptFriend(Integer userId, Integer friendId);

    public void deleteFriend(Integer userId, Integer friendId);

    public List<FriendVO> getListFollower(Integer userId);

    public List<FriendVO> getListFollowing(Integer userId);

    public boolean isFriend(Integer userId, Integer friendId);

    public long getCountFollower(Integer userId);

    public long getCountFollowing(Integer userId);

    public List<FriendVO> searchFriend(UserSearchCondition userSearchCondition, Pageable pageable);


}
