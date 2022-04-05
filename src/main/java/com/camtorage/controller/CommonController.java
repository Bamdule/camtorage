package com.camtorage.controller;

import com.camtorage.db.appconfig.service.AppConfigService;
import com.camtorage.db.geartype.service.GearTypeService;
import com.camtorage.entity.version.Device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/common")
public class CommonController {

    @Autowired
    private AppConfigService appConfigService;

    @ApiOperation(value = "공통 설정 정보", notes = "공통 설정 정보")
    @GetMapping("/config")
    public ResponseEntity getConfig(
        @RequestParam(value = "device", required = false) Device device
    ) {

        return ResponseEntity.ok(appConfigService.getAppConfig(device));
    }

}
