package com.project.vestiart.services.database;

import com.project.vestiart.models.Role;
import com.project.vestiart.repositories.IRoleRepository;
import com.project.vestiart.services.interfaces.IRoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleService implements IRoleService {

    private final IRoleRepository IRoleRepository;

    public RoleService(IRoleRepository IRoleRepository) {
        this.IRoleRepository = IRoleRepository;
    }

    @Override
    public Role getRoleById(int id) {
        return IRoleRepository.getRoleById(id);
    }
}
