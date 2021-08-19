package com.camtorage.db.user.service;

import com.camtorage.entity.user.UserTO;
import com.camtorage.entity.user.UserToken;
import com.camtorage.entity.user.UserUpdateTO;
import com.camtorage.entity.user.UserVO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    public UserTO saveUser(UserTO userTO);

    public void updateUser(UserUpdateTO userUpdateTO);

    public String updateUserImage(Integer userId, MultipartFile file);

    public void deleteUserImage(Integer userId);

    public UserToken loginUser(String email, String password);

    public UserVO getUser(Integer id);
}
