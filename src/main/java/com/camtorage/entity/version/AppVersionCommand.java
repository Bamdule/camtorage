package com.camtorage.entity.version;

import lombok.Builder;
import lombok.Getter;

public class AppVersionCommand {

    @Getter
    public static class Save {
        private Device device;

        private String MinimumVersion;

        @Builder
        public Save(Device device, String minimumVersion) {
            this.device = device;
            MinimumVersion = minimumVersion;
        }

        public AppVersion toEntity() {
            return AppVersion
                .builder()
                .device(this.getDevice())
                .minimumVersion(this.MinimumVersion)
                .build();
        }

    }
}
