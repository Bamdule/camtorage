package com.camtorage.controller;

import com.camtorage.db.appconfig.service.AppConfigService;
import com.camtorage.db.geartype.service.GearTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/common")
public class CommonController {

    @Autowired
    private AppConfigService appConfigService;

    @GetMapping("/config")
    public ResponseEntity getConfig() {

        return ResponseEntity.ok(appConfigService.getAppConfig());
    }


}
