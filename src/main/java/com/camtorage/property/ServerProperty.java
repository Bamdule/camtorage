package com.camtorage.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ServerProperty {

    private AwsS3Property awsS3Property;

}
