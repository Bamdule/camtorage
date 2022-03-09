package com.camtorage.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.camtorage.controller.dto.ForgottenInfoRequestDto;
import com.camtorage.db.user.forgotteninfo.ForgottenInfoCommand;
import com.camtorage.db.user.forgotteninfo.ForgottenInfoService;

@RestController
@RequestMapping(value = "/api/forgotten-info")
public class ForgottenInfoController {

    @Autowired
    private ForgottenInfoService forgottenInfoService;

    @PatchMapping("/user/password")
    public ResponseEntity<Void> updateForgottenPassword(
        @Valid ForgottenInfoRequestDto.UpdatePassword updatePasswordRequest
    ) {

        ForgottenInfoCommand.UpdatePassword command = updatePasswordRequest.toCommand();

        forgottenInfoService.updatePassword(command);

        return ResponseEntity.noContent().build();
    }
}
