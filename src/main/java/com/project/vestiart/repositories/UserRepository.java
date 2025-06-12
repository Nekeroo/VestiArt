package com.project.vestiart.repositories;

import com.project.vestiart.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    boolean existsUserByUsername(String username);

}
