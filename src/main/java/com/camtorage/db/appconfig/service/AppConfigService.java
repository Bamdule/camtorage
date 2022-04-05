package com.camtorage.db.appconfig.service;

import com.camtorage.entity.common.AppConfigVO;
import com.camtorage.entity.version.Device;

public interface AppConfigService {

    public AppConfigVO getAppConfig(Device device);
}
