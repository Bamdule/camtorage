package com.camtorage.common.util;

import com.camtorage.property.AwsS3Property;
import com.camtorage.property.ServerProperty;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

/**
 * @author kim
 */
@Component
public class PathUtil {

    @Autowired
    private ServerProperty serverProperty;

    public String createPath(String subDir, String filename) {
        return new StringBuilder()
                .append(serverProperty.getAwsS3Property().getResourceDir())
                .append("/")
                .append(subDir)
                .append("/")
                .append(filename)
                .toString();
    }

    //파일 유형(1) + 일련번호(10) + 컨텐츠해상도(2)+ '_' + 랜덤번호+ 확장자
    public String createFileName(String ext) {
        return new StringBuilder()
                .append(LocalDate.now().toString())
                .append("_")
                .append(RandomStringUtils.randomAlphanumeric(10))
                .append(".")
                .append(ext)
                .toString();
    }


    public String getUrlWithDomain(String path) {
        if (Strings.isNotBlank(path)) {
            return new StringBuilder()
                    .append(serverProperty.getAwsS3Property().getDomain())
                    .append(path)
                    .toString();
        } else {
            return "";
        }
    }
}
