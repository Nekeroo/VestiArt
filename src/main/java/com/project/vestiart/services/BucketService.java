package com.project.vestiart.services;

import com.project.vestiart.models.BucketInfos;
import com.project.vestiart.models.dto.BucketDTO;
import com.project.vestiart.models.input.RequestInput;
import com.project.vestiart.repositories.BucketInfosRepository;
import com.project.vestiart.utils.mappers.MultipartFileMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BucketService {

    @Qualifier("bucketWebClient")
    private final WebClient bucketWebClient;

    private final MultipartFileMapper multipartFileMapper;

    private final BucketInfosRepository bucketInfosRepository;

    public BucketService(WebClient bucketWebClient, MultipartFileMapper multipartFileMapper, BucketInfosRepository bucketInfosRepository) {
        this.bucketWebClient = bucketWebClient;
        this.multipartFileMapper = multipartFileMapper;
        this.bucketInfosRepository = bucketInfosRepository;
    }

    public BucketInfos uploadFileFromGeneration(RequestInput input, byte[] image) throws IOException {

        UUID uidGenerated = generateUID();

        return bucketWebClient
                .post()
                .uri("/student/upload")
                .body(multipartFileMapper.getMultiPartInsert(image, input, uidGenerated))
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

        UUID id = UUID.randomUUID();
        return id;
    }


}
