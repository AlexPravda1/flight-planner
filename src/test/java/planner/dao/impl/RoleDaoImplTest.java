package planner.dao.impl;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import planner.AbstractTest;
import planner.dao.RoleDao;
import planner.exception.DataProcessingException;
import planner.model.Role;
import planner.model.UserRoleName;

class RoleDaoImplTest extends AbstractTest {
    @Autowired
    private RoleDao roleDao;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role(UserRoleName.USER);
    }

    @Test
    void save_validData_thenCorrect() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
        roleDao.delete(actual.getId());
    }

    @Test
    void get_duplicatedRole_thenException() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
        Assertions.assertThrows(DataProcessingException.class, () -> roleDao.save(role),
                "Expected DataProcessingException when saving Role which already exist in DB");
        roleDao.delete(actual.getId());
    }

    @Test
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    void getRoleByName_validData_thenCorrect() {
        Role savedRole = roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(role.getRoleName().name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(savedRole.getRoleName(), actual.get().getRoleName());
        roleDao.delete(savedRole.getId());
    }

    @Test
    void getRoleByName_nonExistentRole_thenException() {
        Role savedRole = roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(UserRoleName.ADMIN.name());
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.isEmpty());
        roleDao.delete(savedRole.getId());
    }
}
