package com.camtorage.entity.version;

import java.util.Optional;

public interface AppVersionReader {

    Optional<AppVersion> findAppVersionByDevice(Device device);

}
