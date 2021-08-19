package com.camtorage.property;

import lombok.*;
import org.springframework.stereotype.Component;


@Getter
public class AwsS3Property {

    protected AwsS3Property(String accessKey, String secretKey, String region, String bucket, String resourceDir, String domain) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.region = region;
        this.bucket = bucket;
        this.resourceDir = resourceDir;
        this.domain = domain;
    }

    private String accessKey;

    private String secretKey;

    private String region;

    private String bucket;

    private String resourceDir;

    private String domain;

}
