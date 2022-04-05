package com.camtorage.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.camtorage.controller.dto.AppVersionRequestDto;
import com.camtorage.controller.dto.AppVersionResponseDto;
import com.camtorage.entity.version.AppVersionCommand;
import com.camtorage.entity.version.AppVersionInfo;
import com.camtorage.entity.version.AppVersionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@RequestMapping(value = "/api/app/version")
@RestController
public class AppVersionController {

    private final AppVersionService appVersionService;

    @PostMapping
    ResponseEntity<AppVersionResponseDto.AppVersionResponse> saveAppVersion(
        @Valid AppVersionRequestDto.AppVersionSaveRequest request
    ) {

        AppVersionCommand.Save command = request.to();

        AppVersionInfo.Main main = appVersionService.saveAppVersion(command);

        AppVersionResponseDto.AppVersionResponse response = AppVersionResponseDto.AppVersionResponse.of(main);

        return ResponseEntity.ok(response);
    }

}
