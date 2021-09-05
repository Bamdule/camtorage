package com.camtorage.db.friend.repository;

import com.camtorage.entity.friend.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Integer>, FriendRepositoryCustom {

    public void deleteFriendByUserIdAndFriendId(Integer userId, Integer friendId);



}
