package com.camtorage.controller.dto;

import java.time.LocalDateTime;

import com.camtorage.entity.version.AppVersionInfo;
import com.camtorage.entity.version.Device;

import lombok.Builder;
import lombok.Getter;

public class AppVersionResponseDto {

    @Getter
    public static class AppVersionResponse {
        private Long id;
        private Device device;
        private String minimumVersion;
        private LocalDateTime createDate;
        private LocalDateTime updateDate;

        @Builder
        public AppVersionResponse(Long id, Device device, String minimumVersion, LocalDateTime createDate,
            LocalDateTime updateDate) {
            this.id = id;
            this.device = device;
            this.minimumVersion = minimumVersion;
            this.createDate = createDate;
            this.updateDate = updateDate;
        }

        public static AppVersionResponse of(AppVersionInfo.Main main) {

            return AppVersionResponse.builder()
                .id(main.getId())
                .device(main.getDevice())
                .minimumVersion(main.getMinimumVersion())
                .createDate(main.getCreateDate())
                .updateDate(main.getUpdateDate())
                .build();
        }

    }
}
