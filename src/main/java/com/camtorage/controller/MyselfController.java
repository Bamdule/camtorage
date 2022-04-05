package com.camtorage.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.camtorage.aop.LoginUser;
import com.camtorage.controller.dto.FriendRequestDto;
import com.camtorage.controller.dto.UserRequestDto;
import com.camtorage.db.friend.service.FriendService;
import com.camtorage.db.gear.service.GearService;
import com.camtorage.entity.user.UserCommand;
import com.camtorage.db.user.UserService;
import com.camtorage.entity.Pages;
import com.camtorage.entity.friend.FriendVO;
import com.camtorage.entity.gear.GearImageWrap;
import com.camtorage.entity.gear.GearRequest;
import com.camtorage.entity.gear.GearResponse;
import com.camtorage.entity.user.UserPayload;
import com.camtorage.entity.user.UserUpdateTO;
import com.camtorage.entity.user.UserWrapperVO;
import com.camtorage.exception.CustomException;
import com.camtorage.exception.ExceptionCode;

import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/api/myself")
public class MyselfController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private GearService gearService;

    @Autowired
    private FriendService friendService;

    /*
    내 정보 조회
     */
    @GetMapping
    public ResponseEntity getUser(@LoginUser UserPayload userPayload) {
        UserWrapperVO userWrapperVO = userService.getUserInfo(userPayload.getUserId());

        return ResponseEntity.ok(userWrapperVO);
    }

    /*
    내 정보 수정
     */
    @PutMapping
    public ResponseEntity updateUser(@LoginUser UserPayload userPayload, UserUpdateTO userUpdateTO) {

        userUpdateTO.setId(userPayload.getUserId());
        userService.updateUser(userUpdateTO);

        return ResponseEntity.noContent().build();
    }

    /*
    내 정보 수정
     */
    @PutMapping(value = "/password")
    public ResponseEntity updateUserPassword(
        @ApiParam(hidden = true) @LoginUser UserPayload userPayload,
        String password
    ) {

        userService.updatePassword(userPayload.getUserId(), password);

        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/password/certification")
    public ResponseEntity certifyPassword(
        @LoginUser UserPayload userPayload,
        UserRequestDto.CreatePasswordCertification createPasswordCertification
    ) {
        UserCommand.VerifyPasswordCertification command = createPasswordCertification.toCommand(userPayload.getUserId());

        boolean isCertification = userService.certifyPassword(command);

        if (!isCertification) {
            throw new CustomException(ExceptionCode.PASSWORD_CERTIFICATION_INVALID);
        }

        return ResponseEntity.noContent().build();
    }

    /*
    내 프로필 사진 등록
     */
    @PostMapping(value = "/image")
    public ResponseEntity updateUserImage(@LoginUser UserPayload userPayload, MultipartFile userImage) {
        String url = userService.updateUserImage(userPayload.getUserId(), userImage);
        return ResponseEntity.ok(url);
    }

    /*
    내 프로필 사진 삭제
     */
    @DeleteMapping(value = "/image")
    public ResponseEntity deleteUserImage(@LoginUser UserPayload userPayload) {
        userService.deleteUserImage(userPayload.getUserId());
        return ResponseEntity.noContent().build();
    }

    /*
    내 장비 등록
     */
    @PostMapping(value = "/gear")
    public ResponseEntity saveGear(@LoginUser UserPayload userPayload, @Valid GearRequest gear,
        List<MultipartFile> gearImages) {

        GearResponse gearResponse = gearService.saveGear(userPayload.getUserId(), gear, gearImages);

        return ResponseEntity.ok(gearResponse);
    }

    /*
    내 장비 수정
     */
    @PutMapping(value = "/gear/{gearId}")
    public ResponseEntity updateGear(
        @LoginUser UserPayload userPayload,
        @PathVariable(value = "gearId") Integer gearId,
        GearRequest gear,
        @ModelAttribute(value = "gearImages") GearImageWrap gearImageWrap
    ) {
        gearService.updateGear(userPayload.getUserId(), gearId, gear, gearImageWrap.getGearImages());

        return ResponseEntity.noContent().build();
    }

    /*
    내 장비 삭제
     */
    @DeleteMapping(value = "/gear/{gearId}")
    public ResponseEntity deleteGear(@LoginUser UserPayload userPayload,
        @PathVariable(value = "gearId") Integer gearId) {
        gearService.deleteGear(gearId);
        return ResponseEntity.noContent().build();
    }

    /*
    내 장비 조회
     */
    @GetMapping(value = "/gear")
    public ResponseEntity<List<GearResponse>> getListGear(@LoginUser UserPayload userPayload) {
        return ResponseEntity.ok(gearService.getListGear(userPayload.getUserId()));
    }

    /*
    친구 요청
     */
    @PostMapping(value = "/friend/{friendId}")
    public ResponseEntity requestFriend(
        @LoginUser UserPayload userPayload,
        @PathVariable Integer friendId
    ) {
        friendService.requestFriend(
            userPayload.getUserId(),
            friendId
        );

        return ResponseEntity.noContent().build();
    }

    /*
    친구 요청 승인
     */
    @PutMapping(value = "/friend/{friendId}")
    public ResponseEntity acceptFriend(
        @LoginUser UserPayload userPayload,
        @PathVariable Integer friendId
    ) {
        friendService.acceptFriend(
            userPayload.getUserId(),
            friendId
        );

        return ResponseEntity.noContent().build();
    }

    /*
    친구 삭제
     */
    @DeleteMapping(value = "/friend/{friendId}")
    public ResponseEntity deleteFriend(
        @LoginUser UserPayload userPayload,
        @PathVariable Integer friendId
    ) {
        friendService.deleteFriend(
            userPayload.getUserId(),
            friendId
        );
        return ResponseEntity.noContent().build();
    }

    /*
    내 팔로워 조회
     */
    @GetMapping(value = "/friend/follower")
    public ResponseEntity<Pages<FriendVO>> getListFollower(
        @LoginUser UserPayload userPayload,
        FriendRequestDto.SearchRequest searchRequest,
        Pageable pageable
    ) {
        Pages<FriendVO> friends = friendService.findFollowersByUserId(
            userPayload.getUserId(),
            searchRequest,
            pageable
        );

        return ResponseEntity.ok(friends);
    }

    /*
    내 팔로잉 조회
     */
    @GetMapping(value = "/friend/following")
    public ResponseEntity getListFollowing(
        @LoginUser UserPayload userPayload,
        FriendRequestDto.SearchRequest searchRequest,
        Pageable pageable
    ) {
        Pages<FriendVO> friends = friendService.findFollowingsByUserId(
            userPayload.getUserId(),
            searchRequest,
            pageable
        );

        return ResponseEntity.ok(friends);
    }

    /*
    친구 삭제
     */
    @DeleteMapping
    public ResponseEntity deleteUser(
        @LoginUser UserPayload userPayload
    ) {
        userService.deleteUserById(
            userPayload.getUserId()
        );

        return ResponseEntity.noContent().build();
    }

}
