package com.camtorage.property;

import com.camtorage.aws.AwsS3;
import com.camtorage.db.serverconfig.service.ServerConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class PropertyConfig {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ServerConfigService serverConfigService;

    @Autowired
    private ServerProperty serverProperty;

    @Autowired
    private AwsS3 awsS3;

    @PostConstruct
    public void staticPropertyBean() {
        Map<String, String> serviceConfigMap = serverConfigService.getServiceConfigMap();

        AwsS3Property awsS3Property = new AwsS3Property(
                serviceConfigMap.get("aws_s3_accesskey"),
                serviceConfigMap.get("aws_s3_secretkey"),
                serviceConfigMap.get("aws_s3_region"),
                serviceConfigMap.get("aws_s3_bucket"),
                serviceConfigMap.get("aws_s3_resource_dir"),
                serviceConfigMap.get("aws_s3_domain")
        );

        awsS3.init(
                awsS3Property.getAccessKey(),
                awsS3Property.getSecretKey(),
                awsS3Property.getRegion(),
                awsS3Property.getBucket()
        );

        serverProperty.setAwsS3Property(awsS3Property);


    }
}
