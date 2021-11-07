package com.camtorage.db.friend.service;

import com.camtorage.db.friend.repository.FriendRepository;
import com.camtorage.domain.user.repository.UserRepository;
import com.camtorage.entity.friend.Friend;
import com.camtorage.domain.user.dto.search.UserSearchCondition;
import com.camtorage.entity.friend.FriendRelationship;
import com.camtorage.entity.friend.FriendStatus;
import com.camtorage.entity.friend.FriendVO;
import com.camtorage.entity.user.User;
import com.camtorage.exception.CustomException;
import com.camtorage.exception.ExceptionCode;
import com.camtorage.property.AwsS3Property;
import com.camtorage.property.ServerProperty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FriendServiceImpl implements FriendService {
    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServerProperty serverProperty;

    @Override
    public void requestFriend(Integer userId, Integer friendId) {

        Optional<Friend> friendOptional = friendRepository.findFriendByUserIdAndFriendId(
            userId,
            friendId
        );

        if (friendOptional.isPresent()) {
            if (friendOptional.get().getStatus().equals(FriendStatus.FRIEND)) {
                throw new CustomException(ExceptionCode.FRIEND_ALREADY_COMPLETION);
            } else {
                throw new CustomException(ExceptionCode.FRIEND_ALREADY_REQUESTED);
            }
        }

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new CustomException(ExceptionCode.USER_NOT_EXISTED);
        }

        Optional<User> optionalFriend = userRepository.findById(friendId);

        if (optionalFriend.isEmpty()) {
            throw new CustomException(ExceptionCode.USER_NOT_EXISTED);
        }

        User user = optionalUser.get();
        User friendUser = optionalFriend.get();

        Friend friend = Friend.builder()
            .user(user)
            .friend(friendUser)
            .status(FriendStatus.REQUEST)
            .build();

        friendRepository.save(friend);
    }

    @Override
    public void acceptFriend(Integer userId, Integer friendId) {
        Friend friend = friendRepository.findFriendByUserIdAndFriendIdAndStatus(
            friendId,
            userId,
            FriendStatus.REQUEST
        ).orElseThrow(() -> {
            throw new CustomException(ExceptionCode.FRIEND_NOT_EXISTED);
        });

        friend.setStatus(FriendStatus.FRIEND);
        friendRepository.save(friend);

        try {
            Friend user = Friend.builder()
                .user(friend.getFriend())
                .friend(friend.getUser())
                .status(FriendStatus.FRIEND)
                .build();

            friendRepository.save(user);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            Optional<Friend> userOptional = friendRepository.findFriendByUserIdAndFriendId(
                userId,
                friendId
            );
            Friend user = userOptional.get();
            user.setStatus(FriendStatus.FRIEND);
            friendRepository.save(user);
        }
    }

    @Override
    @Transactional
    public void deleteFriend(Integer userId, Integer friendId) {
        friendRepository.deleteFriendByUserIdAndFriendId(userId, friendId);

        Optional<Friend> friendOptional = friendRepository.findFriendByUserIdAndFriendIdAndStatus(
            friendId,
            userId,
            FriendStatus.FRIEND
        );

        if (friendOptional.isPresent()) {
            Friend friend = friendOptional.get();
            friend.setStatus(FriendStatus.REQUEST);
            friendRepository.save(friend);
        }
    }

    @Override
    public List<FriendVO> getListFollower(Integer userId) {
        List<FriendVO> friendVOS = friendRepository.getListFollower(userId);
        return friendVOS;
    }

    @Override
    public List<FriendVO> getListFollowing(Integer userId) {
        List<FriendVO> friendVOS = friendRepository.getListFollowing(userId);
        return friendVOS;
    }

    @Override
    public boolean isFriend(Integer userId, Integer friendId) {
        Optional<Friend> optionalFriend = friendRepository.findFriendByUserIdAndFriendId(userId, friendId);

        if (optionalFriend.isEmpty()) {
            return false;
        } else if (optionalFriend.get().getStatus().equals(FriendStatus.FRIEND)) {
            return true;
        }

        return false;
    }

    @Override
    public long getCountFollower(Integer userId) {
        return friendRepository.getCountFollower(userId);
    }

    @Override
    public long getCountFollowing(Integer userId) {
        return friendRepository.getCountFollowing(userId);
    }

    @Override
    public List<FriendVO> searchFriend(UserSearchCondition userSearchCondition, Pageable pageable) {

        return null;
    }

    @Override
    public FriendRelationship getFriendRelationship(Integer myUserId, Integer otherUserId) {
        Optional<Friend> my = friendRepository.findFriendByUserIdAndFriendId(myUserId, otherUserId);

        if (my.isPresent()) {
            Friend friend = my.get();
            if (friend.getStatus().equals(FriendStatus.FRIEND)) {
                return FriendRelationship.FRIEND;
            } else {
                return FriendRelationship.FOLLOWING;
            }
        }

        Optional<Friend> other = friendRepository.findFriendByUserIdAndFriendId(otherUserId, myUserId);

        if (other.isPresent()) {
            return FriendRelationship.FOLLOWER;
        }

        return FriendRelationship.NONE;
    }

}
