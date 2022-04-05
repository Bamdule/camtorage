package com.camtorage.infrastructure.version;

import org.springframework.stereotype.Component;

import com.camtorage.entity.version.AppVersion;
import com.camtorage.entity.version.AppVersionStore;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AppVersionStoreImpl implements AppVersionStore {

    private final AppVersionRepository appVersionRepository;

    @Override
    public AppVersion save(AppVersion appVersion) {
        return appVersionRepository.save(appVersion);
    }
}
