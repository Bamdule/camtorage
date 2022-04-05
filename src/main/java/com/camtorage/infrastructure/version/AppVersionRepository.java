package com.camtorage.infrastructure.version;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.camtorage.entity.version.AppVersion;
import com.camtorage.entity.version.Device;

public interface AppVersionRepository extends JpaRepository<AppVersion, Long> {

    Optional<AppVersion> findAppVersionByDevice(Device device);
}
