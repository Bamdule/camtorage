package com.camtorage.entity.common;

import com.camtorage.entity.geartype.GearTypeVO;
import com.camtorage.entity.version.AppVersionInfo;
import com.camtorage.entity.version.Device;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

import java.util.List;

@Getter
@Setter
public class AppConfigVO {

    private List<GearTypeVO> gearTypes;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AppVersion appVersion;

    public void setAppVersion(AppVersionInfo.Main appVersion) {
        this.appVersion = AppVersion.builder()
            .device(appVersion.getDevice())
            .minimumVersion(appVersion.getMinimumVersion())
            .build();
    }

    @Getter
    public static class AppVersion {
        private Device device;

        private String minimumVersion;

        @Builder
        public AppVersion(Device device, String minimumVersion) {
            this.device = device;
            this.minimumVersion = minimumVersion;
        }
    }
}
