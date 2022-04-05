package com.camtorage.infrastructure.version;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.camtorage.entity.version.AppVersion;
import com.camtorage.entity.version.AppVersionReader;
import com.camtorage.entity.version.Device;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AppVersionReaderImpl implements AppVersionReader {

    private final AppVersionRepository appVersionRepository;

    @Override
    public Optional<AppVersion> findAppVersionByDevice(Device device) {
        return appVersionRepository.findAppVersionByDevice(device);
    }
}
