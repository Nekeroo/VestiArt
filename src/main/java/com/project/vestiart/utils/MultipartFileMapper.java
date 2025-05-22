package com.project.vestiart.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.vestiart.models.input.RequestInput;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class MultipartFileMapper {

    public MultipartFile mapDataToMultipartFile(RequestInput input, File image)
    {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonData = objectMapper.writeValueAsString(input);

            FileInputStream imageInputStream = new FileInputStream(image);
            MultipartFile imageFile = new MockMultipartFile(
                    "image",                        // Name
                    image.getName(),                // Original filename
                    "image/png",                   // Content type
                    imageInputStream                // File content
            );

            // Optionally, create a multipart for JSON part if needed
            MultipartFile jsonPart = new MockMultipartFile(
                    "data",                         // Name of JSON part
                    "data.json",                    // Filename
                    "application/json",             // Content type
                    jsonData.getBytes()             // File content
            );

            // You can either return one of them, or wrap them in a structure depending on usage
            // For example, if you're sending both parts in a REST request, return both in a list or custom class
            return imageFile;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
