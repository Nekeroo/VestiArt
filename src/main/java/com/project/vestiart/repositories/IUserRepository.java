package com.project.vestiart.repositories;

import com.project.vestiart.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    boolean existsUserByUsername(String username);

}
