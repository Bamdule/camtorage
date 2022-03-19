package com.camtorage.controller;

import java.net.URI;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.camtorage.aop.LoginUser;
import com.camtorage.controller.dto.UserRequestDto;
import com.camtorage.db.user.UserService;
import com.camtorage.domain.user.dto.UserResponseDto;
import com.camtorage.domain.user.dto.search.UserSearchCondition;
import com.camtorage.domain.user.dto.search.UserSearchResponse;
import com.camtorage.entity.user.UserCommand;
import com.camtorage.entity.user.UserInfo;
import com.camtorage.entity.user.UserPayload;
import com.camtorage.entity.user.UserToken;
import com.camtorage.entity.user.UserWrapperVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/user")
@RequiredArgsConstructor
public class UserController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    final private UserService userService;

    @GetMapping("/existEmail")
    public ResponseEntity isExistEmail(String email) {
        return ResponseEntity.ok(userService.isExistEmail(email));
    }

    /**
     * 회원 가입
     * @param createRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<UserResponseDto.CreateResponse> createUser(
        @Valid UserRequestDto.CreateRequest createRequest
    ) {
        UserCommand.Create createCommand = createRequest.toCommand();
        UserInfo.Create createdUser = userService.createUser(createCommand);

        UserResponseDto.CreateResponse response = UserResponseDto.CreateResponse.of(createdUser);

        return ResponseEntity.created(URI.create("/api/user/" + createdUser.getId())).body(response);
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
     * @param otherUserId
     * @return
     */
    @GetMapping(value = "/{otherUserId}")
    public ResponseEntity getUser(
        @LoginUser UserPayload userPayload,
        @PathVariable(value = "otherUserId") Integer otherUserId) {
        UserWrapperVO userWrapperVO = userService.getOtherUserInfo(userPayload.getUserId(), otherUserId);

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
