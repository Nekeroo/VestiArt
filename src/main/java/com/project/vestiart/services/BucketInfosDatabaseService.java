package com.project.vestiart.services;

import com.project.vestiart.models.BucketInfos;
import com.project.vestiart.repositories.BucketInfosRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BucketInfosDatabaseService {

    private final BucketInfosRepository bucketInfosRepository;

    public BucketInfosDatabaseService(BucketInfosRepository bucketInfosRepository) {
        this.bucketInfosRepository = bucketInfosRepository;
    }

    public List<BucketInfos> getAll() {
        List<BucketInfos> listBucketInfos = new ArrayList<>();

        Iterable<BucketInfos> listFound = bucketInfosRepository.findAll();

        for (BucketInfos bucketInfos : listFound) {
            listBucketInfos.add(bucketInfos);
        }

        return listBucketInfos;
    }

    public void addBucketInfos(BucketInfos bucketInfos) {
        bucketInfosRepository.save(bucketInfos);
    }

    public Optional<BucketInfos> getBucketInfosByIdExterne(String idExterne){
        return bucketInfosRepository.findByidExterne(idExterne);
    }

    public void removeBucketInfos(BucketInfos bucketInfos) {
        bucketInfosRepository.delete(bucketInfos);
    }
}
