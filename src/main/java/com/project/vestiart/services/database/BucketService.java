package com.project.vestiart.services.database;

import com.project.vestiart.models.BucketInfos;
import com.project.vestiart.utils.mappers.MultipartFileMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Service
public class BucketService {

    @Qualifier("bucketWebClient")
    private final WebClient bucketWebClient;

    private final MultipartFileMapper multipartFileMapper;

    public BucketService(WebClient bucketWebClient, MultipartFileMapper multipartFileMapper) {
        this.bucketWebClient = bucketWebClient;
        this.multipartFileMapper = multipartFileMapper;
    }

    public BucketInfos uploadFileFromGeneration(String tag1, String tag2, byte[] document, String fileType) {

        UUID uidGenerated = generateUID();

        return bucketWebClient
                .post()
                .uri("/student/upload")
                .body(multipartFileMapper.getMultiPartInsert(document, tag1, tag2 , uidGenerated, fileType))
                .retrieve()
                .bodyToMono(BucketInfos.class)
                .block();
    }

    public void deleteDocumentFromTheBucket(String uid) {
        bucketWebClient
                .delete()
                .uri("/student/delete/" + uid);
    }

    public UUID generateUID() {
        return UUID.randomUUID();
    }


}
