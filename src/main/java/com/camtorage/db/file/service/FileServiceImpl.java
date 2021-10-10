package com.camtorage.db.file.service;

import com.camtorage.aws.S3Directory;
import com.camtorage.aws.S3Info;
import com.camtorage.aws.S3Service;
import com.camtorage.db.image.repository.ImageRepository;
import com.camtorage.db.image.service.ImageService;
import com.camtorage.entity.image.Image;
import com.camtorage.exception.CustomException;
import com.camtorage.exception.ExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private S3Service s3Service;


    @Transactional
    @Override
    public List<Image> saveFiles(List<MultipartFile> files, S3Directory dir) {
        List<Image> images = new ArrayList<>();
        List<S3Info> s3Infos = new ArrayList<>();

        try {
            s3Infos = s3Service.uploadFiles(files, dir);
            for (S3Info s3Info : s3Infos) {
                Image image = imageService.saveImage(s3Info);
                images.add(image);
            }
        } catch (IOException e) {
            s3Infos.forEach(info -> {
                s3Service.deleteFile(info.getPath());
            });
            throw new CustomException(ExceptionCode.AWS_S3_UPLOAD_ERROR);

        }


        return images;
    }

    @Transactional
    @Override
    public Image saveFile(MultipartFile file, S3Directory dir) {
        S3Info s3Info = null;
        Image image = null;
        try {
            s3Info = s3Service.uploadFile(file, dir);
            image = imageService.saveImage(s3Info);
        } catch (IOException e) {
            if (s3Info != null) {
                s3Service.deleteFile(s3Info.getPath());
            }
        }
        return image;
    }

    @Transactional
    @Override
    public Image updateFile(MultipartFile file, S3Directory dir, Integer imageId) {
        Optional<Image> optionalImage = imageRepository.findById(imageId);

        if (optionalImage.isEmpty()) {
            throw new CustomException(ExceptionCode.IMAGE_ID_NOT_EXISTED);
        }

        Image image = optionalImage.get();
        String beforeImagePath = image.getPath();

        S3Info s3Info = null;
        try {
            s3Info = s3Service.uploadFile(file, dir);
            imageService.updateImage(image.getId(), s3Info);
        } catch (IOException e) {
            if (s3Info != null) {
                s3Service.deleteFile(s3Info.getPath());
            }
        }

        s3Service.deleteFile(beforeImagePath);

        return image;
    }

    @Override
    public void deleteFile(Integer imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow(() -> {
            throw new CustomException(ExceptionCode.IMAGE_ID_NOT_EXISTED);
        });

        s3Service.deleteFile(image.getPath());
        imageService.deleteImage(imageId);
    }
}
