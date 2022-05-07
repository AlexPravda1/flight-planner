package planner.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import planner.dao.RoleDao;
import planner.exception.DataProcessingException;
import planner.model.Role;
import planner.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Role save(Role role) {
        return roleDao.save(role);
    }

    @Override
    public Role getRoleByName(String roleName) {
        return roleDao.getRoleByName(roleName).orElseThrow(() -> new DataProcessingException(
                "Role with name: " + roleName + " was not found in DB."));
    }

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }
}
