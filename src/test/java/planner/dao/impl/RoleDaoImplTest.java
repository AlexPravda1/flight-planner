package planner.dao.impl;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import planner.AbstractTest;
import planner.dao.RoleDao;
import planner.exception.DataProcessingException;
import planner.model.Role;
import planner.model.UserRoleName;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class, UserRoleName.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(UserRoleName.USER);
    }

    @Test
    void save_validData_thenCorrect() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void get_duplicatedRole_thenException() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
        Assertions.assertThrows(DataProcessingException.class, () -> roleDao.save(role),
                "Expected DataProcessingException when saving Role which already exist in DB");
    }

    @Test
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    void getRoleByName_validData_thenCorrect() {
        Role savedRole = roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(role.getRoleName().name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(savedRole.getRoleName(), actual.get().getRoleName());
    }

    @Test
    void getRoleByName_nonExistentRole_thenException() {
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(UserRoleName.ADMIN.name());
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.isEmpty());
    }
}
