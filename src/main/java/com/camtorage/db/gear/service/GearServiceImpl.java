package com.camtorage.db.gear.service;

import com.camtorage.aws.S3Directory;
import com.camtorage.db.file.service.FileService;
import com.camtorage.db.gear.repository.GearImageRepository;
import com.camtorage.db.gear.repository.GearRepository;
import com.camtorage.db.user.repository.UserRepository;
import com.camtorage.entity.gear.*;
import com.camtorage.entity.image.Image;
import com.camtorage.entity.user.User;
import com.camtorage.exception.CustomException;
import com.camtorage.exception.ExceptionCode;
import com.camtorage.property.AwsS3Property;
import com.camtorage.property.ServerProperty;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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

    @Autowired
    private ModelMapper modelMapper;


    @Transactional
    @Override
    public GearResponse saveGear(Integer userId, GearRequest gearRequest, List<MultipartFile> files) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_EXISTED));

        Gear gear = modelMapper.map(gearRequest, Gear.class);
        gear.setUser(user);
        gear.setCreateDt(LocalDateTime.now());
        gear.setUpdateDt(LocalDateTime.now());

        gear = gearRepository.save(gear);

        List<Image> images = fileService.saveFiles(files, S3Directory.GEAR);

        for (Image image : images) {

            GearImage gearImage = GearImage.builder()
                    .gear(gear)
                    .image(image)
                    .build();

            gearImageRepository.save(gearImage);
        }

        return modelMapper.map(gear, GearResponse.class);
    }

    @Transactional
    @Override
    public void updateGear(Integer userId, Integer gearId, GearRequest gearRequest, List<GearImageTO> gearImages) {


        Gear gear = gearRepository
                .findById(gearId)
                .orElseThrow(() -> new CustomException(ExceptionCode.GEAR_NOT_EXISTED));

        gear.setGearTypeId(gearRequest.getGearTypeId());
        gear.setCapacity(gearRequest.getCapacity());
        gear.setColor(gearRequest.getColor());
        gear.setCompany(gearRequest.getCompany());
        gear.setName(gearRequest.getName());
        gear.setPrice(gearRequest.getPrice());
        gear.setBuyDt(gearRequest.getBuyDt());
        gear.setUpdateDt(LocalDateTime.now());

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
    public List<GearResponse> getListGear(Integer userId) {

        List<GearResponse> gears = gearRepository.getListGear(userId);

        return gears;
    }

    @Override
    public List<GearImageVO> getListGearImage(Integer userId, Integer gearId) {
        List<GearImageVO> gearImages = gearRepository.getListGearImage(gearId);
        AwsS3Property awsS3Property = serverProperty.getAwsS3Property();

        gearImages.forEach(gearImage -> {
            if (gearImage.getUrl() != null) {
                gearImage.setUrl(new StringBuilder()
                        .append(awsS3Property.getDomain())
                        .append(gearImage.getUrl())
                        .toString()
                );
            }

        });

        return gearImages;
    }

    @Override
    public long getCountGear(Integer userId) {
        return gearRepository.getCountGear(userId);
    }
}
