package com.project.vestiart.repositories;


import com.project.vestiart.models.Idea;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdeaRepository extends CrudRepository<Idea, Long> {

}
