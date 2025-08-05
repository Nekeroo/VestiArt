package com.project.vestiart.repositories;


import com.project.vestiart.models.Idea;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IIdeaRepository extends CrudRepository<Idea, Long> {

    List<Idea> findAllByOrderByDateCreateDesc(Pageable pageRequest);
}
