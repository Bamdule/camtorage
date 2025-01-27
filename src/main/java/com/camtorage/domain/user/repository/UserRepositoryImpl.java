package com.camtorage.domain.user.repository;

import com.camtorage.domain.user.dto.UserResponseDto;
import com.camtorage.domain.user.dto.search.UserSearchCondition;
import com.camtorage.domain.user.dto.search.UserSearchResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

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
    public Optional<UserResponseDto> getUserById(Integer id) {

        JPAQueryFactory query = new JPAQueryFactory(em);

        return Optional.ofNullable(query
            .select(Projections.bean(
                UserResponseDto.class,
                user.id,
                user.name,
                user.email,
                user.phone,
                user.aboutMe,
                user.isPublic.as("isPublic"),
                user.image.id.as("userImageId"),
                user.image.path.prepend(s3domain).as("userImageUrl")
            ))
            .from(user)
            .leftJoin(user.image)
            .where(
                user.id.eq(id),
                user.isDelete.isFalse()
            )
            .fetchOne())
            ;

    }

    public long searchUserTotalCount(UserSearchCondition userSearchCondition) {
        JPAQueryFactory query = new JPAQueryFactory(em);

        String searchQuery = userSearchCondition.getSearchText().concat("%");

        return query.selectFrom(user)
            .where(
                user.name.like(searchQuery).or(user.email.like(searchQuery)),
                user.isDelete.isFalse()
            )
            .fetchCount();
    }

    @Override
    public UserSearchResponse searchUser(UserSearchCondition userSearchCondition, Pageable pageable) {
        JPAQueryFactory query = new JPAQueryFactory(em);

        long totalCount = this.searchUserTotalCount(userSearchCondition);

        String searchQuery = userSearchCondition.getSearchText().concat("%");

        StringExpression userImageUrl = new CaseBuilder()
            .when(user.image.id.isNotNull())
            .then(user.image.path.prepend(s3domain))
            .otherwise("");

        List<UserResponseDto> userResponsDtos = query
            .select(Projections.bean(
                UserResponseDto.class,
                user.id,
                user.name,
                user.email,
                user.phone,
                user.isPublic.as("isPublic"),
                user.image.id.as("userImageId"),
                userImageUrl.as("userImageUrl")
            ))
            .from(user)
            .leftJoin(user.image)
            .where(
                user.name.like(searchQuery).or(user.email.like(searchQuery)),
                user.isDelete.isFalse()
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return UserSearchResponse
            .builder()
            .users(userResponsDtos)
            .page(pageable.getPageNumber())
            .size(pageable.getPageSize())
            .total(totalCount)
            .build();
    }
}
