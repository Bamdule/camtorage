package com.camtorage.db.file.service;

import com.camtorage.aws.S3Directory;
import com.camtorage.aws.S3Info;
import com.camtorage.aws.S3Service;
import com.camtorage.db.image.repository.ImageRepository;
import com.camtorage.db.image.service.ImageService;
import com.camtorage.entity.image.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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


    @Override
    public List<Image> saveFiles(List<MultipartFile> files, S3Directory dir) {
        List<S3Info> s3Infos = s3Service.uploadFiles(files, dir);

        List<Image> images = new ArrayList<>();
        for (S3Info s3Info : s3Infos) {
            Image image = imageService.saveImage(s3Info);
            images.add(image);
        }

        return images;
    }

    @Override
    public Image saveFile(MultipartFile file, S3Directory dir) {
        S3Info s3Info = s3Service.uploadFile(file, dir);
        Image image = imageService.saveImage(s3Info);
        return image;
    }

    @Override
    public Image updateFile(MultipartFile file, S3Directory dir, Integer imageId) {
        Optional<Image> optionalImage = imageRepository.findById(imageId);

        if (optionalImage.isEmpty()) {
            throw new RuntimeException();
        }

        Image image = optionalImage.get();
        String beforeImagePath = image.getPath();

        S3Info s3Info = s3Service.uploadFile(file, dir);
        imageService.updateImage(image.getId(), s3Info);

        s3Service.deleteFile(beforeImagePath);
        return image;
    }

    @Override
    public void deleteFile(Integer imageId) {
        Optional<Image> optionalImage = imageRepository.findById(imageId);

        if (optionalImage.isEmpty()) {
            throw new RuntimeException();
        }

        Image image = optionalImage.get();

        s3Service.deleteFile(image.getPath());
        imageService.deleteImage(imageId);
    }
}
