package com.camtorage.db.file.service;

import com.camtorage.aws.S3Directory;
import com.camtorage.entity.image.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    public List<Image> saveFiles(List<MultipartFile> files, S3Directory dir);

    public Image saveFile(MultipartFile file, S3Directory dir);

    public Image updateFile(MultipartFile file, S3Directory dir, Integer imageId);

    public void deleteFile(Integer imageId);


}
