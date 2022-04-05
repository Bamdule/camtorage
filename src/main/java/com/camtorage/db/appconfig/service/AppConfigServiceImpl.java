package com.camtorage.db.appconfig.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.camtorage.db.geartype.service.GearTypeService;
import com.camtorage.entity.common.AppConfigVO;
import com.camtorage.entity.geartype.GearTypeVO;
import com.camtorage.entity.version.AppVersionInfo;
import com.camtorage.entity.version.AppVersionService;
import com.camtorage.entity.version.Device;

@Service
public class AppConfigServiceImpl implements AppConfigService {

    @Autowired
    private GearTypeService gearTypeService;

    @Autowired
    private AppVersionService appVersionService;

    @Override
    public AppConfigVO getAppConfig(Device device) {
        AppConfigVO appConfig = new AppConfigVO();

        List<GearTypeVO> gearTypes = gearTypeService.getListGearType();
        appConfig.setGearTypes(gearTypes);

        if (Objects.nonNull(device)) {
            AppVersionInfo.Main appVersion = appVersionService.getAppVersion(device);
            appConfig.setAppVersion(appVersion);
        }

        return appConfig;
    }
}
