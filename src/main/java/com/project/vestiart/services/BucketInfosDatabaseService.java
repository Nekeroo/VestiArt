package com.project.vestiart.services;

import com.project.vestiart.models.BucketInfos;
import com.project.vestiart.repositories.BucketInfosRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        bucketInfos.setDateCreate(LocalDateTime.now());
        bucketInfosRepository.save(bucketInfos);
    }

    public Optional<BucketInfos> getBucketInfosByIdExterne(String idExterne){
        return bucketInfosRepository.findByidExterne(idExterne);
    }

    public void removeBucketInfos(BucketInfos bucketInfos) {
        bucketInfosRepository.delete(bucketInfos);
    }

    public List<BucketInfos> retrieveLastBucketInfos(int numberOfElements) {
        Pageable pageRequest = PageRequest.of(0, numberOfElements);
        List<BucketInfos> bucketInfos = bucketInfosRepository.findAllByOrderByDateCreateDesc(pageRequest);
        return bucketInfos;
    }

    public List<BucketInfos> retrievePaginatedBucketInfos(int page, int size) {
        List<BucketInfos> bucketInfos = new ArrayList<>();
        Iterable<BucketInfos> allBucketInfos = bucketInfosRepository.findAll();
        int start = page * size;
        int end = Math.min(start + size, (int) bucketInfosRepository.count());
        for (int i = start; i < end; i++) {
            bucketInfos.add((BucketInfos) allBucketInfos);
        }
        return bucketInfos;
    }
}
