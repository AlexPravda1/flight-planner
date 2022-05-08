package planner.dao;

import java.util.List;
import java.util.Optional;
import planner.model.Role;

public interface RoleDao {
    Role save(Role role);

    Optional<Role> getRoleByName(String roleName);

    List<Role> findAll();
}
