package com.camtorage.entity.version;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.camtorage.exception.CustomException;
import com.camtorage.exception.ExceptionCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AppVersionServiceImpl implements AppVersionService {

    private final AppVersionStore appVersionStore;
    private final AppVersionReader appVersionReader;

    @Transactional
    @Override
    public AppVersionInfo.Main saveAppVersion(AppVersionCommand.Save saveCommand) {
        AppVersion appVersion = appVersionReader.findAppVersionByDevice(saveCommand.getDevice())
            .orElseGet(() -> null);

        if (Objects.isNull(appVersion)) {
            appVersion = saveCommand.toEntity();
        } else {
            appVersion.updateAppVersion(saveCommand.getDevice(), saveCommand.getMinimumVersion());
        }

        AppVersion saveAppVersion = appVersionStore.save(appVersion);

        return AppVersionInfo.Main.of(saveAppVersion);
    }

    @Override
    public AppVersionInfo.Main getAppVersion(Device device) {
        AppVersion appVersion = appVersionReader.findAppVersionByDevice(device)
            .orElseThrow(() -> new CustomException(ExceptionCode.DEVICE_NOT_FOUND));

        return AppVersionInfo.Main.of(appVersion);
    }
}
