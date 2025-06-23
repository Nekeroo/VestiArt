package com.project.vestiart.repositories;

import com.project.vestiart.models.RequestInput;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestInputRepository extends CrudRepository<RequestInput, Long> {
}
