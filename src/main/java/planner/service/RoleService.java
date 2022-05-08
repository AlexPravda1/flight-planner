package planner.service;

import java.util.List;
import planner.model.Role;

public interface RoleService {
    Role save(Role role);

    Role getRoleByName(String roleName);

    List<Role> findAll();
}
