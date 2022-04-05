package com.camtorage.entity.version;

public interface AppVersionService {

    AppVersionInfo.Main saveAppVersion(AppVersionCommand.Save saveCommand);

    AppVersionInfo.Main getAppVersion(Device device);

}
