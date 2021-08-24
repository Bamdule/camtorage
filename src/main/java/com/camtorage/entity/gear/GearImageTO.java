package com.camtorage.entity.gear;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class GearImageTO {
    private Integer imageId;

    private MultipartFile image;
}
