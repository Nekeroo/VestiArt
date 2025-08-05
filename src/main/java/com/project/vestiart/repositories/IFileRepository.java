package com.project.vestiart.repositories;

import com.project.vestiart.models.File;
import org.springframework.data.repository.CrudRepository;

public interface IFileRepository extends CrudRepository<File, Long> {

}
