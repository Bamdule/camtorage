package com.camtorage.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.camtorage.entity.version.AppVersionCommand;
import com.camtorage.entity.version.Device;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class AppVersionRequestDto {

    @ToString
    @Setter
    @Getter
    public static class AppVersionSaveRequest {

        @NotNull(message = "디바이스는 필수 값 입니다.")
        private Device device;

        @NotBlank(message = "최소 버전은 필수 값 입니다.")
        private String minimumVersion;

        public AppVersionCommand.Save to() {
            return AppVersionCommand.Save.builder()
                .device(this.device)
                .minimumVersion(this.minimumVersion)
                .build();
        }
    }

}
