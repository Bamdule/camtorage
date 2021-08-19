package com.camtorage.aws;

import com.camtorage.common.util.PathUtil;
import com.camtorage.exception.CustomException;
import com.camtorage.exception.ExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class S3ServiceImpl implements S3Service {

    @Autowired
    private AwsS3 awsS3;

    @Autowired
    private PathUtil pathUtil;

    @Override
    public S3Info uploadFile(MultipartFile file, S3Directory dir) {

        try {
            return this.uploadToS3(file, dir);
        } catch (IOException e) {
            throw new CustomException(ExceptionCode.AWS_S3_UPLOAD_ERROR);
        }
    }

    @Override
    public List<S3Info> uploadFiles(List<MultipartFile> files, S3Directory dir) {


        List<S3Info> s3Infos = new ArrayList<>();

        for (MultipartFile file : files) {
            try {

                S3Info s3Info = this.uploadToS3(file, dir);
                s3Infos.add(s3Info);

            } catch (IOException e) {

                s3Infos.forEach(info -> {
                    awsS3.delete(info.getPath());
                });

                throw new CustomException(ExceptionCode.AWS_S3_UPLOAD_ERROR);
            }

        }

        return s3Infos;
    }

    @Override
    public void deleteFile(String path) {
        awsS3.delete(path);
    }

    private S3Info uploadToS3(MultipartFile file, S3Directory dir) throws IOException {
        if (Objects.isNull(file)) {
            throw new CustomException(ExceptionCode.FILE_NOT_EXISTED);
        }
        if (!file.getContentType().startsWith("image/")) {
            throw new CustomException(ExceptionCode.NO_IMAGE);
        }

        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);
        String filename = pathUtil.createFileName(extension);
        String path = pathUtil.createPath(dir.name(), filename);

        awsS3.upload(file, path);

        return S3Info.builder()
                .path(path)
                .orgFilename(file.getOriginalFilename())
                .build();
    }
}
