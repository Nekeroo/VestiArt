package com.project.vestiart.controllers;

import com.project.vestiart.models.BucketInfos;
import com.project.vestiart.dto.BucketDTO;
import com.project.vestiart.services.BucketInfosDatabaseService;
import com.project.vestiart.services.BucketService;
import com.project.vestiart.utils.mappers.BucketInfosMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bucket")
public class BucketController {

    private final BucketInfosDatabaseService bucketInfosDatabaseService;
    private final BucketInfosMapper bucketInfosMapper;
    private final BucketService bucketService;

    public BucketController(BucketInfosDatabaseService bucketInfosDatabaseService, BucketInfosMapper bucketInfosMapper, BucketService bucketService) {
        this.bucketInfosDatabaseService = bucketInfosDatabaseService;
        this.bucketInfosMapper = bucketInfosMapper;
        this.bucketService = bucketService;
    }

    @GetMapping("/retrieve/all")
    public List<BucketDTO> retrieveAllBucketInfos() {
        List<BucketInfos> bucketInfos =  bucketInfosDatabaseService.getAll();

        List<BucketDTO> resultList = new ArrayList<>();

        for (BucketInfos bi : bucketInfos) {
            resultList.add(bucketInfosMapper.mapBucketInfosToBucketDTO(bi));
        }

        return resultList;
    }

    @GetMapping("/retrieve/{uid}")
    public BucketDTO retrieBucketInfosByUid(@PathVariable String uid) {
        Optional<BucketInfos> bucketInfos = bucketInfosDatabaseService.getBucketInfosByIdExterne(uid);
        return bucketInfos.map(bucketInfosMapper::mapBucketInfosToBucketDTO).orElse(null);
    }

    @PostMapping("/add")
    public BucketDTO addBucketInfos(@RequestBody BucketDTO bucketDTO) {
        BucketInfos bucketInfos = bucketInfosMapper.mapBucketDTOToBucketInfos(bucketDTO);
        bucketInfosDatabaseService.addBucketInfos(bucketInfos);
        return bucketDTO;
    }

    @DeleteMapping("/delete/{uid}")
    public void removeBucketInfos(@PathVariable String uid) {
        Optional<BucketInfos> bucketInfos = bucketInfosDatabaseService.getBucketInfosByIdExterne(uid);
        if (bucketInfos.isPresent()) {
            bucketInfosDatabaseService.removeBucketInfos(bucketInfos.get());
            bucketService.deleteDocumentFromTheBucket(uid);
        } else {
            throw new RuntimeException("BucketInfos not found");
        }
    }

    @GetMapping("/retrieve/last/{numberOfElements}")
    public List<BucketDTO> retrieveLastBucketInfos(@PathVariable int numberOfElements) {
        List<BucketInfos> bucketInfos = bucketInfosDatabaseService.retrieveLastBucketInfos(numberOfElements);
        List<BucketDTO> resultList = new ArrayList<>();

        for (BucketInfos bi : bucketInfos) {
            resultList.add(bucketInfosMapper.mapBucketInfosToBucketDTO(bi));
        }

        return resultList;
    }

    @GetMapping("/retrieve/{page}/{size}")
    public ResponseEntity<List<BucketDTO>> retrievePaginatedBucketInfos(@PathVariable int page, @PathVariable int size) {
        List<BucketInfos> bucketInfos = bucketInfosDatabaseService.retrievePaginatedBucketInfos(page, size);
        List<BucketDTO> resultList = new ArrayList<>();

        for (BucketInfos bi : bucketInfos) {
            resultList.add(bucketInfosMapper.mapBucketInfosToBucketDTO(bi));
        }

        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

}
