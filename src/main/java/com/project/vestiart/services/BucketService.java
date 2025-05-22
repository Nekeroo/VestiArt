package com.project.vestiart.services;

import com.project.vestiart.models.BucketInfos;
import com.project.vestiart.models.input.RequestInput;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.BodyInserters.MultipartInserter;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileInputStream;
import java.io.IOException;

@Service
public class BucketService {

    @Qualifier("bucketWebClient")
    private final WebClient bucketWebClient;

    public BucketService(WebClient bucketWebClient) {
        this.bucketWebClient = bucketWebClient;
    }

    public BucketInfos uploadFileFromGeneration(RequestInput input, byte[] image) throws IOException {


        return bucketWebClient
                .post()
                .uri("/student/upload")
                .body(getMultiPartInsert(image, input))
                .retrieve()
                .bodyToMono(BucketInfos.class)
                .block();
    }

    private static MultipartInserter getMultiPartInsert(byte[] file, RequestInput input) throws IOException {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        ByteArrayResource ressource = new ByteArrayResource(file) {
            @Override
            public String getFilename() {
                return "coucou.png";
            }
        } ;

        builder.part("file", ressource);
        builder.part("idExterne", 2);
        builder.part("tag1", input.getPerson());
        builder.part("tag2", input.getReference());
        builder.part("tag3", input.getType());

        return BodyInserters.fromMultipartData(builder.build());
    }
}
