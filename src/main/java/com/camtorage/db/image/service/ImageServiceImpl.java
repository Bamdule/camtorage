package com.camtorage.db.image.service;

import com.camtorage.aws.S3Info;
import com.camtorage.db.image.repository.ImageRepository;
import com.camtorage.entity.image.Image;
import com.camtorage.exception.CustomException;
import com.camtorage.exception.ExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public Image saveImage(S3Info s3Info) {
        Image image = Image.builder()
                .orgFilename(s3Info.getOrgFilename())
                .path(s3Info.getPath())
                .build();
        imageRepository.save(image);

        return image;
    }

    @Override
    public Image updateImage(Integer imageId, S3Info s3Info) {

        Image image = Image.builder()
                .id(imageId)
                .orgFilename(s3Info.getOrgFilename())
                .path(s3Info.getPath())
                .build();
        imageRepository.save(image);
        return image;
    }

    @Override
    public void deleteImage(Integer imageId) {
        imageRepository.deleteById(imageId);
    }
}
