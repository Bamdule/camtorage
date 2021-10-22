package com.camtorage.db.friend.repository;

import com.camtorage.entity.friend.*;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static com.camtorage.entity.friend.QFriend.friend1;

public class FriendRepositoryImpl implements FriendRepositoryCustom {

    @Autowired
    private EntityManager em;

    @Override
    public Optional<Friend> findFriendByUserIdAndFriendId(Integer userId, Integer friendId) {
        JPAQueryFactory query = new JPAQueryFactory(em);

        Friend friend = query.select(friend1)
            .from(friend1)
            .where(
                friend1.user.id.eq(userId),
                friend1.friend.id.eq(friendId)
            )
            .fetchOne();
        ;

        return Optional.ofNullable(friend);
    }

    @Override
    public Optional<Friend> findFriendByUserIdAndFriendIdAndStatus(Integer userId, Integer friendId,
        FriendStatus status) {
        JPAQueryFactory query = new JPAQueryFactory(em);

        Friend friend = query.select(friend1)
            .from(friend1)
            .where(
                friend1.user.id.eq(userId),
                friend1.friend.id.eq(friendId),
                friend1.status.eq(status)
            )
            .fetchOne();
        ;

        return Optional.ofNullable(friend);
    }

    @Override
    public List<FriendVO> getListFollower(Integer userId) {
        JPAQueryFactory query = new JPAQueryFactory(em);

        return query.select(
                Projections.bean(
                    FriendVO.class,
                    friend1.id,
                    friend1.status,
                    friend1.user.id.as("friendId"),
                    friend1.user.name.as("name"),
                    friend1.user.email.as("email"),
                    friend1.user.image.path.as("profilePath")
                ))
            .from(friend1)
            .join(friend1.user)
            .leftJoin(friend1.user.image)
            .where(
                friend1.friend.id.eq(userId)
            )
            .orderBy(friend1.status.desc())
            .fetch()
            ;
    }

    @Override
    public List<FriendVO> getListFollowing(Integer userId) {
        JPAQueryFactory query = new JPAQueryFactory(em);

        return query.select(
                Projections.bean(
                    FriendVO.class,
                    friend1.id,
                    friend1.status,
                    friend1.friend.id.as("friendId"),
                    friend1.friend.name.as("name"),
                    friend1.friend.email.as("email"),
                    friend1.friend.image.path.as("profilePath")
                ))
            .from(friend1)
            .join(friend1.friend)
            .leftJoin(friend1.friend.image)
            .where(
                friend1.user.id.eq(userId)
            )
            .orderBy(friend1.status.desc())
            .fetch()
            ;
    }

    @Override
    public long getCountFollower(Integer userId) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        return query.select(friend1)
            .from(friend1)
            .where(friend1.friend.id.eq(userId))
            .fetchCount();
    }

    @Override
    public long getCountFollowing(Integer userId) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        return query.select(friend1)
            .from(friend1)
            .where(friend1.user.id.eq(userId))
            .fetchCount();
    }

}
