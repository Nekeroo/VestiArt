package com.project.vestiart.repositories;

import com.project.vestiart.models.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Role getRoleById(long id);

}
