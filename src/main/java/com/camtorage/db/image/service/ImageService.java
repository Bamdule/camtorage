package com.camtorage.db.image.service;

import com.camtorage.aws.S3Info;
import com.camtorage.entity.image.Image;
import com.camtorage.entity.image.ImageTO;

public interface ImageService {

    public Image saveImage(S3Info s3Info);

    public Image updateImage(Integer imageId, S3Info s3Info);

    public void deleteImage(Integer imageId);
}
