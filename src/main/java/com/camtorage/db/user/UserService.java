package com.camtorage.db.user;

import com.camtorage.domain.user.dto.UserResponseDto;
import com.camtorage.domain.user.dto.search.UserSearchCondition;
import com.camtorage.domain.user.dto.search.UserSearchResponse;
import com.camtorage.entity.user.*;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    public boolean isExistEmail(String email);

    public UserInfo.Create createUser(UserCommand.Create createCommand);

    public void updateUser(UserUpdateTO userUpdateTO);

    public void updatePassword(Integer userId, String password);

    boolean certifyPassword(UserCommand.VerifyPasswordCertification command);

    public String updateUserImage(Integer userId, MultipartFile file);

    public void deleteUserImage(Integer userId);

    public UserToken loginUser(String email, String password);

    public UserResponseDto getUser(Integer id);

    public UserWrapperVO getUserInfo(Integer id);

    public UserWrapperVO getOtherUserInfo(Integer myUserId, Integer otherUserId);

    public Boolean isPublic(Integer id);

    public UserSearchResponse searchUser(UserSearchCondition userSearchCondition, Pageable pageable);

    public void deleteUserById(Integer id);
}
