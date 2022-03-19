package com.camtorage.db.user;

import java.time.LocalDateTime;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.camtorage.aws.S3Directory;
import com.camtorage.common.util.PathUtil;
import com.camtorage.db.certification.email.EmailCertificationService;
import com.camtorage.db.file.service.FileService;
import com.camtorage.db.friend.service.FriendService;
import com.camtorage.db.gear.service.GearService;
import com.camtorage.domain.user.dto.UserResponseDto;
import com.camtorage.domain.user.dto.search.UserSearchCondition;
import com.camtorage.domain.user.dto.search.UserSearchResponse;
import com.camtorage.domain.user.repository.UserRepository;
import com.camtorage.entity.image.Image;
import com.camtorage.entity.user.User;
import com.camtorage.entity.user.UserCommand;
import com.camtorage.entity.user.UserInfo;
import com.camtorage.entity.user.UserPayload;
import com.camtorage.entity.user.UserToken;
import com.camtorage.entity.user.UserUpdateTO;
import com.camtorage.entity.user.UserWrapperVO;
import com.camtorage.exception.CustomException;
import com.camtorage.exception.ExceptionCode;
import com.camtorage.jwt.UserJWT;

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

    @Autowired
    private EmailCertificationService emailCertificationService;

    @Override
    public boolean isExistEmail(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }

    @Override
    public UserInfo.Create createUser(UserCommand.Create createCommand) {

        /**
         * 이메일 중복을 체크한다.
         */
        userRepository
            .findUserByEmail(createCommand.getEmail())
            .ifPresent((user) -> {
                throw new CustomException(ExceptionCode.ALREADY_JOINED);
            });

        User initUser = User.builder()
            .email(createCommand.getEmail())
            .name(createCommand.getName())
            .password(passwordEncoder.encode(createCommand.getPassword()))
            .joinDt(LocalDateTime.now())
            .isPublic(true)
            .isDelete(false)
            .build();

        User createdUser = userRepository.save(initUser);

        UserInfo.Create userInfo = UserInfo.Create.of(createdUser);

        return userInfo;
    }

    @Transactional
    @Override
    public void updateUser(UserUpdateTO userUpdateTO) {

        User user = userRepository
            .findById(userUpdateTO.getId())
            .orElseThrow(() -> {
                throw new CustomException(ExceptionCode.USER_NOT_EXISTED);
            });

        user.setName(userUpdateTO.getName());
        user.setPhone(userUpdateTO.getPhone());
        user.setAboutMe(userUpdateTO.getAboutMe());
        user.setIsPublic(userUpdateTO.getIsPublic());

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updatePassword(Integer userId, String password) {
        User user = userRepository
            .findById(userId)
            .orElseThrow(() -> {
                throw new CustomException(ExceptionCode.USER_NOT_EXISTED);
            });

        user.setPassword(passwordEncoder.encode(password));
    }

    @Override
    public boolean certifyPassword(UserCommand.VerifyPasswordCertification command) {
        User user = userRepository
            .findById(command.getUserId())
            .orElseThrow(() -> {
                throw new CustomException(ExceptionCode.USER_NOT_EXISTED);
            });

        return passwordEncoder.matches(command.getPassword(), user.getPassword());
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
        User user = userRepository.findUserByEmail(email)
            .orElseThrow(() -> {
                throw new CustomException(ExceptionCode.LOGIN_FAILED);
            });

        if (user.getIsDelete()) {
            throw new CustomException(ExceptionCode.USER_DELETED);
        }

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
    public UserResponseDto getUser(Integer id) {

        UserResponseDto user = userRepository
            .getUserById(id)
            .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_EXISTED));

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
            .build();
    }

    @Override
    public UserWrapperVO getOtherUserInfo(Integer myUserId, Integer otherUserId) {
        UserWrapperVO userInfo = this.getUserInfo(otherUserId);
        userInfo.setStatus(friendService.getFriendRelationship(myUserId, otherUserId));
        return userInfo;
    }

    @Override
    public Boolean isPublic(Integer id) {
        return userRepository.findUserByIdAndIsPublic(id, true).isPresent();
    }

    @Override
    public UserSearchResponse searchUser(UserSearchCondition userSearchCondition, Pageable pageable) {

        if (StringUtils.hasText(userSearchCondition.getSearchText())) {
            return userRepository.searchUser(userSearchCondition, pageable);
        }
        return new UserSearchResponse();

    }

    @Override
    @Transactional
    public void deleteUserById(Integer id) {
        userRepository.findById(id)
            .orElseThrow(() -> {
                throw new CustomException(ExceptionCode.USER_NOT_EXISTED);
            })
            .setIsDelete(true);

        friendService.deleteAll(id);
    }
}
