package com.camtorage.controller;

import com.camtorage.aop.LoginUser;
import com.camtorage.db.friend.service.FriendService;
import com.camtorage.db.gear.service.GearService;
import com.camtorage.db.user.service.UserService;
import com.camtorage.exception.CustomException;
import com.camtorage.exception.ExceptionCode;
import com.camtorage.entity.user.UserPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/gear")
public class GearController {

    Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private GearService gearService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/{userId}")
    public ResponseEntity getGear(@LoginUser UserPayload userPayload, @PathVariable(value = "userId") Integer userId) {

        if (!(userService.isPublic(userId) || friendService.isFriend(userPayload.getUserId(), userId))) {
            throw new CustomException(ExceptionCode.USER_PRIVATE);
        }

        return ResponseEntity.ok(gearService.getListGear(userId));
    }

}
