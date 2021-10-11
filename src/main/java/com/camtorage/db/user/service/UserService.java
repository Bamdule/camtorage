package com.camtorage.db.user.service;

import com.camtorage.entity.user.*;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    public UserResponse saveUser(UserRequest userRequest);

    public void updateUser(UserUpdateTO userUpdateTO);

    public void updatePassword(Integer userId, String password);

    public String updateUserImage(Integer userId, MultipartFile file);

    public void deleteUserImage(Integer userId);

    public UserToken loginUser(String email, String password);

    public UserResponse getUser(Integer id);

    public UserWrapperVO getUserInfo(Integer id);

    public Boolean isPublic(Integer id);
}
