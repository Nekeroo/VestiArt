package com.project.vestiart.repositories;

import com.project.vestiart.models.BucketInfos;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface BucketInfosRepository extends CrudRepository<BucketInfos, Long> {

    Optional<BucketInfos> findByidExterne(String idExterne);

    List<BucketInfos> findAllByOrderByDateCreateDesc(Pageable pageable);
}
