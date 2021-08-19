package com.camtorage.db.gear.service;

import com.camtorage.aws.S3Directory;
import com.camtorage.aws.S3Info;
import com.camtorage.aws.S3Service;
import com.camtorage.db.file.service.FileService;
import com.camtorage.db.gear.repository.GearImageRepository;
import com.camtorage.db.gear.repository.GearRepository;
import com.camtorage.db.user.repository.UserRepository;
import com.camtorage.db.image.service.ImageService;
import com.camtorage.entity.gear.*;
import com.camtorage.entity.image.Image;
import com.camtorage.entity.user.User;
import com.camtorage.exception.CustomException;
import com.camtorage.exception.ExceptionCode;
import com.camtorage.property.AwsS3Property;
import com.camtorage.property.ServerProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GearServiceImpl implements GearService {
    @Autowired
    private GearRepository gearRepository;

    @Autowired
    private GearImageRepository gearImageRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServerProperty serverProperty;


    @Transactional
    @Override
    public void saveGear(Integer userId, GearTO gearTO, List<MultipartFile> files) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new CustomException(ExceptionCode.USER_NOT_EXISTED);
        }

        Gear gear = Gear.builder()
                .name(gearTO.getName())
                .gearTypeId(gearTO.getGearTypeId())
                .color(gearTO.getColor())
                .capacity(gearTO.getCapacity())
                .company(gearTO.getCompany())
                .gearTypeId(gearTO.getGearTypeId())
                .user(optionalUser.get())
                .build();

        gearRepository.save(gear);

        List<Image> images = fileService.saveFiles(files, S3Directory.GEAR);

        for (Image image : images) {

            GearImage gearImage = GearImage.builder()
                    .gear(gear)
                    .image(image)
                    .build();

            gearImageRepository.save(gearImage);
        }
    }

    @Override
    public List<GearVO> getListGear(Integer userId) {
        AwsS3Property awsS3Property = serverProperty.getAwsS3Property();

        List<GearVO> gears = gearRepository.getListGear(userId);

        for (GearVO gear : gears) {
            List<GearImageVO> gearImages = gearRepository.getListGearImage(gear.getId());

            gearImages.forEach(gearImage -> {
                gearImage.setUrl(new StringBuilder()
                        .append(awsS3Property.getDomain())
                        .append(gearImage.getUrl())
                        .toString()
                );

            });

            gear.setGearImages(gearImages);
        }

        return gears;
    }
}
