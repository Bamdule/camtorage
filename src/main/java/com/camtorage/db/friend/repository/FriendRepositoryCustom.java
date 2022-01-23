package com.camtorage.db.friend.repository;

import com.camtorage.entity.friend.Friend;
import com.camtorage.entity.friend.FriendStatus;
import com.camtorage.entity.friend.FriendVO;

import java.util.List;
import java.util.Optional;

public interface FriendRepositoryCustom {
    public Optional<Friend> findFriendByUserIdAndFriendId(Integer userId, Integer friendId);

    public Optional<Friend> findFriendByUserIdAndFriendIdAndStatus(Integer userId, Integer friendId, FriendStatus status);

    public List<FriendVO> getListFollower(Integer userId);

    public List<FriendVO> getListFollowing(Integer userId);

    public long getCountFollower(Integer userId);

    public long getCountFollowing(Integer userId);

//    public List<FriendVO> searchFriend(UserSearch userSearch, Pageable pageable);

    void deleteAll(Integer id);


}
