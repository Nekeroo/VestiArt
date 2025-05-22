package com.project.vestiart.controllers;

import com.project.vestiart.models.BucketInfos;
import com.project.vestiart.models.dto.BucketDTO;
import com.project.vestiart.services.BucketService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bucket")
public class BucketController {

    private final BucketService bucketService;

    public BucketController(BucketService bucketService) {
        this.bucketService = bucketService;
    }
}
