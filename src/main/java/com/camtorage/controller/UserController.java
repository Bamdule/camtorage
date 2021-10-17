package com.camtorage.controller;

import com.camtorage.aop.LoginUser;
import com.camtorage.db.friend.service.FriendService;
import com.camtorage.domain.user.dto.search.UserSearchCondition;
import com.camtorage.db.user.service.UserService;
import com.camtorage.domain.user.dto.search.UserSearchResponse;
import com.camtorage.entity.user.UserRequest;
import com.camtorage.domain.user.dto.UserResponse;
import com.camtorage.entity.user.UserToken;
import com.camtorage.entity.user.UserWrapperVO;
import com.camtorage.entity.user.UserPayload;
import com.camtorage.exception.CustomException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;
    @Autowired
    private FriendService friendService;

    /**
     * 회원 가입
     * @param user
     * @return
     */
    @PostMapping
    public ResponseEntity joinUser(
        @Valid UserRequest user
    ) {
        UserResponse userResponse = userService.saveUser(user);

        return ResponseEntity.ok().body(userResponse);
    }

    /**
     * 회원 로그인
     * @param email
     * @param password
     * @return
     */
    @PostMapping(value = "/login")
    public ResponseEntity loginUser(String email, String password) {
        UserToken userToken = userService.loginUser(email, password);
        return ResponseEntity.ok(userToken);
    }

    /**
     * ID로 유저 조회
     * @param userPayload
     * @param userId
     * @return
     */
    @GetMapping(value = "/{userId}")
    public ResponseEntity getUser(
        @LoginUser UserPayload userPayload,
        @PathVariable(value = "userId") Integer userId) {
        UserWrapperVO userWrapperVO = userService.getUserInfo(userId);

        // if (!(userWrapperVO.getUser().getIsPublic() || friendService.isFriend(userPayload.getUserId(), userId))) {
        //     throw new CustomException()
        // }

        return ResponseEntity.ok(userWrapperVO);
    }

    /**
     * 유저 검색
     * @param userPayload
     * @param userSearchCondition
     * @param pageable
     * @return
     */
    @GetMapping(value = "/search")
    public ResponseEntity<UserSearchResponse> searchUser(
        @LoginUser UserPayload userPayload,
        UserSearchCondition userSearchCondition, Pageable pageable
    ) {
        UserSearchResponse userSearchResponse = userService.searchUser(userSearchCondition, pageable);

        return ResponseEntity.ok(userSearchResponse);
    }
}
