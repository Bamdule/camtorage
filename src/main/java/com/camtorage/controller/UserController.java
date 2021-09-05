package com.camtorage.controller;

import com.camtorage.aop.LoginUser;
import com.camtorage.db.friend.service.FriendService;
import com.camtorage.db.user.service.UserService;
import com.camtorage.entity.common.ResultVO;
import com.camtorage.entity.user.UserTO;
import com.camtorage.entity.user.UserUpdateTO;
import com.camtorage.entity.user.UserVO;
import com.camtorage.entity.user.UserWrapperVO;
import com.camtorage.jwt.UserJWT;
import com.camtorage.jwt.UserPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/api/user")
public class UserController {


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;
    @Autowired
    private FriendService friendService;

    @PostMapping
    public ResponseEntity joinUser(
            @Valid UserTO user
    ) {

        UserTO userTO = userService.saveUser(user);

        return ResponseEntity.ok().body(userTO);
    }

    @PostMapping(value = "/login")
    public ResponseEntity loginUser(String email, String password) {
        return ResponseEntity.ok(userService.loginUser(email, password));
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity searchUser(@LoginUser UserPayload userPayload, @PathVariable(value = "userId") Integer userId) {
        UserWrapperVO userWrapperVO = userService.getUserInfo(userId);

        if (!(userWrapperVO.getUser().getIsPublic() || friendService.isFriend(userPayload.getUserId(), userId))) {
            userWrapperVO.setCode("rejected");
        }

        return ResponseEntity.ok(userWrapperVO);
    }
}
