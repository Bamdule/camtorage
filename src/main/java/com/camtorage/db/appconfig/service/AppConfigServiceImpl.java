package com.camtorage.db.appconfig.service;

import com.camtorage.db.geartype.service.GearTypeService;
import com.camtorage.entity.common.AppConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppConfigServiceImpl implements AppConfigService {

    @Autowired
    private GearTypeService gearTypeService;

    @Override
    public AppConfigVO getAppConfig() {
        return AppConfigVO.builder()
                .gearTypes(gearTypeService.getListGearType())
                .build();
    }
}
