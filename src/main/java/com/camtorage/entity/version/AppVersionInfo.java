package com.camtorage.entity.version;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

public class AppVersionInfo {

    @Getter
    public static class Main {

        private Long id;

        private Device device;

        private String minimumVersion;

        private LocalDateTime createDate;

        private LocalDateTime updateDate;

        @Builder
        public Main(Long id, Device device, String minimumVersion, LocalDateTime createDate, LocalDateTime updateDate) {
            this.id = id;
            this.device = device;
            this.minimumVersion = minimumVersion;
            this.createDate = createDate;
            this.updateDate = updateDate;
        }

        public static AppVersionInfo.Main of(AppVersion appVersion) {
            return Main
                .builder()
                .id(appVersion.getId())
                .device(appVersion.getDevice())
                .minimumVersion(appVersion.getMinimumVersion())
                .createDate(appVersion.getCreateDate())
                .updateDate(appVersion.getUpdateDate())
                .build();
        }
    }
}
