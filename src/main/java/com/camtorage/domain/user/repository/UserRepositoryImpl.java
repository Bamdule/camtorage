package com.camtorage.domain.user.repository;

import com.camtorage.domain.user.dto.UserResponse;
import com.camtorage.domain.user.dto.search.UserSearchCondition;
import com.camtorage.domain.user.dto.search.UserSearchResponse;
import com.camtorage.property.ServerProperty;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static com.camtorage.entity.user.QUser.user;

public class UserRepositoryImpl implements UserRepositoryCustom {

    @Autowired
    private EntityManager em;

    private String s3domain = "https://camtorage.s3.ap-northeast-2.amazonaws.com/";

    /**
     유저 조회
     */
    @Override
    public Optional<UserResponse> getUserById(Integer id) {

        JPAQueryFactory query = new JPAQueryFactory(em);

        return Optional.ofNullable(query
            .select(Projections.bean(
                UserResponse.class,
                user.id,
                user.name,
                user.email,
                user.phone,
                user.isPublic.as("isPublic"),
                user.image.id.as("userImageId"),
                user.image.path.prepend(s3domain).as("userImageUrl")
            ))
            .from(user)
            .leftJoin(user.image)
            .where(user.id.eq(id))
            .fetchOne())
            ;

    }

    public long searchUserTotalCount(UserSearchCondition userSearchCondition) {
        JPAQueryFactory query = new JPAQueryFactory(em);

        String searchQuery = userSearchCondition.getSearchText().concat("%");

        return query.selectFrom(user)
            .where(
                user.name.like(searchQuery).or(user.email.like(searchQuery))
            )
            .fetchCount();
    }

    @Override
    public UserSearchResponse searchUser(UserSearchCondition userSearchCondition, Pageable pageable) {
        JPAQueryFactory query = new JPAQueryFactory(em);

        long totalCount = this.searchUserTotalCount(userSearchCondition);

        String searchQuery = userSearchCondition.getSearchText().concat("%");

        List<UserResponse> userResponses = query
            .select(Projections.bean(
                UserResponse.class,
                user.id,
                user.name,
                user.email,
                user.phone,
                user.isPublic.as("isPublic"),
                user.image.id.as("userImageId"),
                user.image.path.prepend(s3domain).as("userImageUrl")
            ))
            .from(user)
            .leftJoin(user.image)
            .where(
                user.name.like(searchQuery).or(user.email.like(searchQuery))
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return UserSearchResponse
            .builder()
            .users(userResponses)
            .page(pageable.getPageNumber())
            .size(pageable.getPageSize())
            .total(totalCount)
            .build();
    }
}
