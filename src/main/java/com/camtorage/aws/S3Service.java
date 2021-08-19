package com.camtorage.aws;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface S3Service {

    public S3Info uploadFile(MultipartFile file, S3Directory dir);

    public List<S3Info> uploadFiles(List<MultipartFile> files, S3Directory dir);

    public void deleteFile(String path);

}
