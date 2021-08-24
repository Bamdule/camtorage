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
        Gear gear = gearTO.getGear();
        gear.setUser(optionalUser.get());

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

    @Transactional
    @Override
    public void updateGear(Integer userId, GearTO gearTO, List<GearImageTO> gearImages) {
        if (gearTO.getId() == null) {
            throw new CustomException(ExceptionCode.GEAR_NOT_EXISTED);
        }
        Optional<Gear> optionalGear = gearRepository.findById(gearTO.getId());

        if (optionalGear.isEmpty()) {
            throw new CustomException(ExceptionCode.GEAR_NOT_EXISTED);
        }
        Gear gear = optionalGear.get();

        gear.setGearTypeId(gearTO.getGearTypeId());
        gear.setCapacity(gearTO.getCapacity());
        gear.setColor(gearTO.getColor());
        gear.setCompany(gearTO.getCompany());
        gear.setName(gearTO.getName());
        gear.setPrice(gearTO.getPrice());

        gearRepository.save(gear);

        for (GearImageTO gearImageTO : gearImages) {
            if (gearImageTO.getImageId() == null && gearImageTO.getImage() != null) {
                Image image = fileService.saveFile(gearImageTO.getImage(), S3Directory.GEAR);
                GearImage gearImage = GearImage.builder()
                        .gear(gear)
                        .image(image)
                        .build();

                gearImageRepository.save(gearImage);

            } else if (gearImageTO.getImageId() != null && gearImageTO.getImage() != null) {
                fileService.updateFile(gearImageTO.getImage(), S3Directory.GEAR, gearImageTO.getImageId());
            } else if (gearImageTO.getImageId() != null && gearImageTO.getImage() == null) {
                fileService.deleteFile(gearImageTO.getImageId());
            }
        }

    }

    @Override
    @Transactional
    public void deleteGear(Integer gearId) {
        if (gearId == null) {
            throw new CustomException(ExceptionCode.GEAR_NOT_EXISTED);
        }
        Optional<Gear> optionalGear = gearRepository.findById(gearId);

        if (optionalGear.isEmpty()) {
            throw new CustomException(ExceptionCode.GEAR_NOT_EXISTED);
        }
        Gear gear = optionalGear.get();
        List<GearImage> gearImages = gearImageRepository.findAllByGearId(gearId);

        for (GearImage gearImage : gearImages) {
            gearImageRepository.delete(gearImage);
            fileService.deleteFile(gearImage.getImage().getId());
        }
        gearRepository.delete(gear);
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
