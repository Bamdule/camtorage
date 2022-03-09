package com.camtorage.db.friend.repository;

import static com.camtorage.entity.friend.QFriend.*;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.camtorage.controller.dto.FriendRequestDto;
import com.camtorage.entity.Pages;
import com.camtorage.entity.friend.Friend;
import com.camtorage.entity.friend.FriendStatus;
import com.camtorage.entity.friend.FriendVO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class FriendRepositoryImpl implements FriendRepositoryCustom {

    @Autowired
    private EntityManager em;

    private String s3domain = "https://camtorage.s3.ap-northeast-2.amazonaws.com/";

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
    public Pages<FriendVO> findFollowersByUserId(
        Integer userId,
        FriendRequestDto.SearchRequest searchRequest,
        Pageable pageable
    ) {

        JPAQueryFactory query = new JPAQueryFactory(em);

        StringExpression userImageUrl = new CaseBuilder()
            .when(friend1.user.image.id.isNotNull())
            .then(friend1.user.image.path.prepend(s3domain))
            .otherwise("");

        BooleanBuilder whereBuilder = new BooleanBuilder();

        if (StringUtils.hasText(searchRequest.getSearchText())) {

            final String searchText = searchRequest.getSearchText().concat("%");

            whereBuilder.and(friend1.user.name.like(searchText)
                .or(friend1.user.email.like(searchText)));
        }

        QueryResults<FriendVO> fetchResults = query.select(
                Projections.bean(
                    FriendVO.class,
                    friend1.id,
                    friend1.status,
                    friend1.user.id.as("friendId"),
                    friend1.user.name.as("name"),
                    friend1.user.email.as("email"),
                    userImageUrl.as("profileUrl")
                ))
            .from(friend1)
            .leftJoin(friend1.user.image)
            .where(
                friend1.friend.id.eq(userId),
                friend1.user.isDelete.isFalse(),
                whereBuilder
            )
            .orderBy(friend1.status.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchResults();

        return Pages.<FriendVO>builder()
            .contents(fetchResults.getResults())
            .page(pageable.getPageNumber())
            .size(pageable.getPageSize())
            .total(fetchResults.getTotal())
            .build();
    }

    @Override
    public Pages<FriendVO> findFollowingsByUserId(
        Integer userId,
        FriendRequestDto.SearchRequest searchRequest,
        Pageable pageable
    ) {
        BooleanBuilder whereBuilder = new BooleanBuilder();

        /**
         * 검색어가 있을 경우 이름과 이메일을 조회한다
         */
        if (StringUtils.hasText(searchRequest.getSearchText())) {
            final String searchText = searchRequest.getSearchText().concat("%");
            whereBuilder.and(friend1.friend.name.like(searchText)
                .or(friend1.friend.email.like(searchText)));
        }

        JPAQueryFactory query = new JPAQueryFactory(em);
        StringExpression userImageUrl = new CaseBuilder()
            .when(friend1.friend.image.id.isNotNull())
            .then(friend1.friend.image.path.prepend(s3domain))
            .otherwise("");

        QueryResults<FriendVO> fetchResults = query.select(
                Projections.bean(
                    FriendVO.class,
                    friend1.id,
                    friend1.status,
                    friend1.friend.id.as("friendId"),
                    friend1.friend.name.as("name"),
                    friend1.friend.email.as("email"),
                    userImageUrl.as("profileUrl")
                ))
            .from(friend1)
            .join(friend1.friend)
            .leftJoin(friend1.friend.image)
            .where(
                friend1.user.id.eq(userId),
                friend1.friend.isDelete.isFalse(),
                whereBuilder
            )
            .orderBy(friend1.status.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchResults();

        return Pages.<FriendVO>builder()
            .contents(fetchResults.getResults())
            .page(pageable.getPageNumber())
            .size(pageable.getPageSize())
            .total(fetchResults.getTotal())
            .build();
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

    @Override
    public void deleteAll(Integer id) {
        JPAQueryFactory query = new JPAQueryFactory(em);

        query.delete(friend1)
            .where(friend1.friend.id.eq(id).or(friend1.user.id.eq(id)))
            .execute();
    }

}
