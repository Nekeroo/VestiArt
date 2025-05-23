package com.project.vestiart.utils.mappers;

import com.project.vestiart.models.BucketInfos;
import com.project.vestiart.models.dto.BucketDTO;
import org.springframework.stereotype.Component;

@Component
public class BucketInfosMapper {

    public BucketDTO mapBucketInfosToBucketDTO(BucketInfos bucketInfos) {

        return BucketDTO.builder()
                .idExterne(bucketInfos.getIdExterne())
                .tag1(bucketInfos.getTag1())
                .tag2(bucketInfos.getTag2())
                .tag3(bucketInfos.getTag3())
                .urlFile(bucketInfos.getUrl())
                .dateTime(bucketInfos.getDateCreate())
                .build();
    }

    public BucketInfos mapBucketDTOToBucketInfos(BucketDTO bucketDTO) {

        return BucketInfos.builder()
                .idExterne(bucketDTO.idExterne())
                .tag1(bucketDTO.tag1())
                .tag2(bucketDTO.tag2())
                .tag3(bucketDTO.tag3())
                .url(bucketDTO.urlFile())
                .build();
    }

}
