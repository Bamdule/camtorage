package com.camtorage.controller;

import com.camtorage.aop.LoginUser;
import com.camtorage.db.user.service.UserService;
import com.camtorage.entity.gear.GearTO;
import com.camtorage.entity.user.UserTO;
import com.camtorage.entity.user.UserUpdateTO;
import com.camtorage.entity.user.UserVO;
import com.camtorage.jwt.UserJWT;
import com.camtorage.jwt.UserPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private UserJWT userJWT;

    @GetMapping(value = "/{userId}")
    public ResponseEntity getUser(@PathVariable(value = "userId") Integer userId) {

        return null;
    }

    @GetMapping
    public ResponseEntity getUser(@LoginUser UserPayload userPayload) {
        UserVO user = userService.getUser(userPayload.getUserId());

        return ResponseEntity.ok(user);
    }

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity updateUserImage(@LoginUser UserPayload userPayload, MultipartFile userImage) {
        String url = userService.updateUserImage(userPayload.getUserId(), userImage);
        return ResponseEntity.ok(url);
    }

    @PostMapping
    public ResponseEntity joinUser(
            @Valid UserTO user
    ) {

        UserTO userTO = userService.saveUser(user);

        return ResponseEntity
                .created(linkTo(this.getClass()).slash(1).toUri())
                .body(userTO);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity updateUser(@LoginUser UserPayload userPayload, UserUpdateTO userUpdateTO, MultipartFile userImage) {

        userUpdateTO.setId(userPayload.getUserId());

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadUserImage(@LoginUser UserPayload userPayload, MultipartFile userImage) {


        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/login")
    public ResponseEntity loginUser(String email, String password) {
        return ResponseEntity.ok(userService.loginUser(email, password));
    }
}
