package com.camtorage.controller;

import com.camtorage.aop.LoginUser;
import com.camtorage.aop.UserJWTCheck;
import com.camtorage.db.gear.service.GearService;
import com.camtorage.entity.gear.GearTO;
import com.camtorage.entity.gear.GearVO;
import com.camtorage.jwt.UserPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/api/gear")
public class GearController {

    Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private GearService gearService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity saveGear(@LoginUser UserPayload userPayload, GearTO gear, List<MultipartFile> gearImages) {


        gearService.saveGear(userPayload.getUserId(), gear, gearImages);

        return ResponseEntity.noContent().build();
    }

    @UserJWTCheck
    @GetMapping
    public ResponseEntity<List<GearVO>> getListMyGear(@LoginUser UserPayload userPayload) {

        return ResponseEntity.ok(gearService.getListGear(userPayload.getUserId()));
    }


//    @UserJWTCheck
//    @GetMapping
//    public ResponseEntity<List<GearVO>> getListMyGear(@RequestHeader Map<String, Object> header) {
//        String accessToken = (String) header.get("authorization");
//        UserPayload userPayload = userJWT.verifyJWT(accessToken);
//        return ResponseEntity.ok(gearService.getListGear(userPayload.getUserId()));
//    }


}
