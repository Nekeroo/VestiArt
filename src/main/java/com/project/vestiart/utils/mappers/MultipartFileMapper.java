package com.project.vestiart.utils.mappers;

import com.project.vestiart.models.input.RequestInput;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.client.MultipartBodyBuilder;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.UUID;


@Service
public class MultipartFileMapper {

    public BodyInserters.MultipartInserter getMultiPartInsert(byte[] file, RequestInput input, UUID idExterne) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        ByteArrayResource ressource = new ByteArrayResource(file) {
            @Override
            public String getFilename() {
                return input.getPerson() + ".png";
            }
        } ;

        builder.part("file", ressource);
        builder.part("idExterne", idExterne.toString());
        builder.part("tag1", input.getPerson());
        builder.part("tag2", input.getReference());
        builder.part("tag3", "image");

        return BodyInserters.fromMultipartData(builder.build());
    }


}
