package com.project.vestiart.utils.mappers;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.client.MultipartBodyBuilder;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.BodyInserters.MultipartInserter;

import java.util.UUID;


@Service
public class MultipartFileMapper {

    public MultipartInserter getMultiPartInsert(byte[] file, String tag1, String tag2, UUID idExterne, String fileType) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        ByteArrayResource ressource = new ByteArrayResource(file) {
            @Override
            public String getFilename() {
                return tag1.replace(" ", "_") + fileType;
            }
        } ;

        builder.part("file", ressource);
        builder.part("idExterne", idExterne.toString());
        builder.part("tag1", tag1);
        builder.part("tag2", tag2);
        builder.part("tag3", fileType);

        return BodyInserters.fromMultipartData(builder.build());
    }


}
