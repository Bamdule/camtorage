package com.camtorage.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.camtorage.property.AwsS3Property;
import com.camtorage.property.ServerProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author MW
 */
@Component
public class AwsS3 {

    private AmazonS3 s3Client = null;
    private String bucket = null;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public AwsS3() {
    }

    public void init(String accessKey, String secretKey, String region, String bucket) {
        this.bucket = bucket;

        if (accessKey == null || secretKey == null) {
            return;
        }

        AWSCredentials credentials = new BasicAWSCredentials(
                accessKey,
                secretKey
        );
        this.s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.valueOf(region))
                .build();

        logger.info("[CONFIG] AWS client create Completed");
    }

    public void upload(File file, String key) {
        if (key.startsWith("/")) {
            key = key.substring(1);
        }

        uploadToS3(new PutObjectRequest(this.bucket, key, file));
    }

    public void upload(MultipartFile file, String key) throws IOException {
        if (key.startsWith("/")) {
            key = key.substring(1);
        }

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        uploadToS3(new PutObjectRequest(this.bucket, key, file.getInputStream(), objectMetadata));
    }

    public void upload(InputStream is, String key, long contentLength) {
        if (key.startsWith("/")) {
            key = key.substring(1);
        }

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(contentLength);
        uploadToS3(new PutObjectRequest(this.bucket, key, is, objectMetadata));
    }

    //PutObjectRequest는 Aws S3 버킷에 업로드할 객체 메타 데이터와 파일 데이터로 이루어져있다.
    private void uploadToS3(PutObjectRequest putObjectRequest) {

        this.s3Client.putObject(putObjectRequest);

        logger.info("[AWS] file upload success to s3 : {}", putObjectRequest.getKey());
    }

    public void copy(String orgKey, String copyKey) {
        //Copy 객체 생성
        CopyObjectRequest copyObjRequest = new CopyObjectRequest(
                this.bucket,
                orgKey,
                this.bucket,
                copyKey
        );
        //Copy
        this.s3Client.copyObject(copyObjRequest);
        logger.info("[AWS] Finish copying [{}] to [{}]", orgKey, copyKey);
    }

    public void delete(String key) {
        if (key.startsWith("/")) {
            key = key.substring(1);
        }

        //Delete 객체 생성
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(this.bucket, key);
        //Delete
        this.s3Client.deleteObject(deleteObjectRequest);
        logger.info("[AWS] delete files in s3 : {}", key);
    }

}
