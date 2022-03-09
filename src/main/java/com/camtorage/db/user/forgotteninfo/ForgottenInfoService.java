package com.camtorage.db.user.forgotteninfo;

public interface ForgottenInfoService {

    void updatePassword(ForgottenInfoCommand.UpdatePassword command);
}
