package com.camtorage.controller;

import com.camtorage.aop.LoginUser;
import com.camtorage.db.friend.service.FriendService;
import com.camtorage.db.gear.service.GearService;
import com.camtorage.db.user.service.UserService;
import com.camtorage.entity.friend.FriendVO;
import com.camtorage.entity.friend.FriendWrapper;
import com.camtorage.entity.gear.GearImageWrap;
import com.camtorage.entity.gear.GearRequest;
import com.camtorage.entity.gear.GearResponse;
import com.camtorage.entity.user.UserUpdateTO;
import com.camtorage.entity.user.UserWrapperVO;
import com.camtorage.entity.user.UserPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

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
    @PutMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity updateUser(@LoginUser UserPayload userPayload, UserUpdateTO userUpdateTO) {

        userUpdateTO.setId(userPayload.getUserId());
        userService.updateUser(userUpdateTO);

        return ResponseEntity.noContent().build();
    }

    /*
    내 정보 수정
     */
    @PutMapping(value = "/password", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity updateUserPassword(@LoginUser UserPayload userPayload, String password) {

        userService.updatePassword(userPayload.getUserId(), password);

        return ResponseEntity.noContent().build();
    }

    /*
    내 프로필 사진 등록
     */
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
    @PostMapping(value = "/gear", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity saveGear(@LoginUser UserPayload userPayload, @Valid GearRequest gear, List<MultipartFile> gearImages) {

        GearResponse gearResponse = gearService.saveGear(userPayload.getUserId(), gear, gearImages);

        return ResponseEntity.ok(gearResponse);
    }

    /*
    내 장비 수정
     */
    @PutMapping(value = "/gear/{gearId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
    public ResponseEntity deleteGear(@LoginUser UserPayload userPayload, @PathVariable(value = "gearId") Integer gearId) {
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
    내 장비 이미지 조회
    */
    @GetMapping(value = "/gear/images/{gearId}")
    public ResponseEntity getListGearImage(@LoginUser UserPayload userPayload, @PathVariable(value = "gearId") Integer gearId) {
        return ResponseEntity.ok(gearService.getListGearImage(userPayload.getUserId(), gearId));
    }


    /*
    친구 요청
     */
    @PostMapping(value = "/friend/{friendId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
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
    @PutMapping(value = "/friend/{friendId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
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
    public ResponseEntity getListFollower(
            @LoginUser UserPayload userPayload) {
        List<FriendVO> friends = friendService.getListFollower(userPayload.getUserId());

        return ResponseEntity.ok(
                FriendWrapper.builder()
                        .description("follower")
                        .friends(friends)
                        .build()
        );
    }

    /*
    내 팔로잉 조회
     */
    @GetMapping(value = "/friend/following")
    public ResponseEntity getListFollowing(
            @LoginUser UserPayload userPayload) {
        List<FriendVO> friends = friendService.getListFollowing(userPayload.getUserId());

        return ResponseEntity.ok(
                FriendWrapper.builder()
                        .description("following")
                        .friends(friends)
                        .build()
        );
    }

//    @GetMapping(value = "/friend/search")
//    public ResponseEntity searchFriend
}
