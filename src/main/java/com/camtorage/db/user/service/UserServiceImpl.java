package com.camtorage.db.user.service;

import com.camtorage.aws.S3Directory;
import com.camtorage.aws.S3Info;
import com.camtorage.aws.S3Service;
import com.camtorage.common.util.PathUtil;
import com.camtorage.db.file.service.FileService;
import com.camtorage.db.image.service.ImageService;
import com.camtorage.db.user.repository.UserRepository;
import com.camtorage.entity.image.Image;
import com.camtorage.entity.user.*;
import com.camtorage.exception.CustomException;
import com.camtorage.exception.ExceptionCode;
import com.camtorage.jwt.UserJWT;
import com.camtorage.jwt.UserPayload;
import com.camtorage.property.AwsS3Property;
import com.camtorage.property.ServerProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private FileService fileService;

    @Autowired
    private PathUtil pathUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserJWT userJWT;

    @Override
    public UserTO saveUser(UserTO userTO) {

        if (userRepository.findUserByEmail(userTO.getEmail()).isPresent()) {
            throw new CustomException(ExceptionCode.ALREADY_JOINED);
        }

        User user = User.builder()
                .email(userTO.getEmail())
                .password(passwordEncoder.encode(userTO.getPassword()))
                .joinDt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        userTO.setId(user.getId());
        userTO.setPassword(null);

        return userTO;
    }

    @Transactional
    @Override
    public void updateUser(UserUpdateTO userUpdateTO) {
        Optional<User> optionalUser = userRepository.findById(userUpdateTO.getId());

        if (optionalUser.isEmpty()) {
            throw new CustomException(ExceptionCode.USER_NOT_EXISTED);
        }
        User user = optionalUser.get();

        user.setName(userUpdateTO.getName());
        user.setPhone(userUpdateTO.getPhone());

        if (!userUpdateTO.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userUpdateTO.getPassword()));
        }

        userRepository.save(user);
    }

    @Override
    public String updateUserImage(Integer userId, MultipartFile file) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new CustomException(ExceptionCode.USER_NOT_EXISTED);
        }

        User user = optionalUser.get();

        Integer userImageId = user.getUserImageId();

        Image image = null;

        if (userImageId == null) {
            image = fileService.saveFile(file, S3Directory.USER);

        } else {
            image = fileService.updateFile(file, S3Directory.USER, userImageId);
        }
        user.setImage(image);
        userRepository.save(user);

        return pathUtil.getUrlWithDomain(image.getPath());
    }

    @Override
    public void deleteUserImage(Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new CustomException(ExceptionCode.USER_NOT_EXISTED);
        }
        User user = optionalUser.get();

        if (user.getImage() != null) {
            int imageId = user.getImage().getId();
            user.setImage(null);
            userRepository.save(user);
            fileService.deleteFile(imageId);
        }
    }

    @Override
    public UserToken loginUser(String email, String password) {
        Optional<User> optionalUser = userRepository.findUserByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new CustomException(ExceptionCode.LOGIN_FAILED);
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ExceptionCode.LOGIN_FAILED);
        }

        UserPayload userPayload = UserPayload.builder()
                .userId(user.getId())
                .build();

        String jwt = userJWT.createJWT(userPayload);


        return UserToken.builder()
                .token(jwt)
                .email(email)
                .build();
    }

    @Override
    public UserVO getUser(Integer id) {
        UserVO user = userRepository.getUserById(id);

        if (user == null) {
            throw new CustomException(ExceptionCode.USER_NOT_EXISTED);
        }

        user.setUserImageUrl(pathUtil.getUrlWithDomain(user.getUserImagePath()));

        return user;

    }
}
