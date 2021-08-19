package com.camtorage.aws;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class S3Info {
    private String path;

    private String orgFilename;

}
