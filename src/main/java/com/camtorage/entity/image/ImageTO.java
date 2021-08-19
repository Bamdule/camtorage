package com.camtorage.entity.image;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageTO {
    private Integer id;

    private String orgFilename;

    private String path;
}
