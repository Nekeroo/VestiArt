package com.project.vestiart.models;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class BucketInfos {

    private int idExterne;

    private String url;

    private String tag1;

    private String tag2;

    private String tag3;

}
