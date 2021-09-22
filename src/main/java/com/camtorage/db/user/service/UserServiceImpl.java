package com.camtorage.db.user.service;

import com.camtorage.aws.S3Directory;
import com.camtorage.common.util.PathUtil;
import com.camtorage.db.file.service.FileService;
import com.camtorage.db.friend.service.FriendService;
import com.camtorage.db.gear.service.GearService;
import com.camtorage.db.user.repository.UserRepository;
import com.camtorage.entity.image.Image;
import com.camtorage.entity.user.*;
import com.camtorage.exception.CustomException;
import com.camtorage.exception.ExceptionCode;
import com.camtorage.jwt.UserJWT;
import com.camtorage.jwt.UserPayload;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
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
    private FriendService friendService;

    @Autowired
    private GearService gearService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserJWT userJWT;

    @Override
    public UserResponse saveUser(UserRequest userRequest) {
        userRepository
                .findUserByEmail(userRequest.getEmail())
                .ifPresent((user) -> {
                    throw new CustomException(ExceptionCode.ALREADY_JOINED);
                });

        User user = User.builder()
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .joinDt(LocalDateTime.now())
                .isPublic(true)
                .build();

        userRepository.save(user);

        userRequest.setId(user.getId());
        userRequest.setIsPublic(user.getIsPublic());

        return modelMapper.map(userRequest, UserResponse.class);
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
        user.setIsPublic(userUpdateTO.isPublic());

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
    public UserResponse getUser(Integer id) {

        UserResponse user = userRepository
                .getUserById(id)
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_EXISTED));

        user.setUserImageUrl(pathUtil.getUrlWithDomain(user.getUserImagePath()));

        return user;

    }

    @Override
    public UserWrapperVO getUserInfo(Integer id) {

        return UserWrapperVO.builder()
                .user(this.getUser(id))
                .followerCnt(friendService.getCountFollower(id))
                .followingCnt(friendService.getCountFollowing(id))
                .gearCnt(gearService.getCountGear(id))
                .boardCnt(0)
                .code("ok")
                .build();
    }

    @Override
    public Boolean isPublic(Integer id) {
        return userRepository.findUserByIdAndIsPublic(id, true).isPresent();
    }
}
