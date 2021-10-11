package com.camtorage.domain.user.repository;

import com.camtorage.domain.user.dto.UserResponse;
import com.camtorage.domain.user.dto.search.UserSearchCondition;
import com.camtorage.domain.user.dto.search.UserSearchResponse;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.camtorage.entity.user.QUser.user;

public class UserRepositoryImpl implements UserRepositoryCustom {

    @Autowired
    private EntityManager em;

    /*
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
                        user.image.path.as("userImagePath")
                ))
                .from(user)
                .leftJoin(user.image)
                .where(user.id.eq(id))
                .fetchOne())
                ;

    }

    @Override
    public UserSearchResponse searchUser(UserSearchCondition userSearchCondition, Pageable pageable) {

        JPAQueryFactory query = new JPAQueryFactory(em);

        String searchQuery = userSearchCondition.getSearchText().concat("%");

        QueryResults<UserResponse> results = query
                .select(Projections.bean(
                        UserResponse.class,
                        user.id,
                        user.name,
                        user.email,
                        user.phone,
                        user.isPublic.as("isPublic")
                ))
                .from(user)
                .where(
                        user.name.like(searchQuery).or(user.email.like(searchQuery))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        ;


        return UserSearchResponse
                .builder()
                .users(results.getResults())
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .total(results.getTotal())
                .build();
    }
}
