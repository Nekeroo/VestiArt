package com.project.vestiart.services;

import com.project.vestiart.models.Role;
import com.project.vestiart.repositories.RoleRepository;
import com.project.vestiart.services.interfaces.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getRoleById(int id) {
        return roleRepository.getRoleById(id);
    }
}
