package planner.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import planner.AbstractTest;
import planner.dao.RoleDao;
import planner.exception.DataProcessingException;
import planner.model.Role;
import planner.model.UserRoleName;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoleDaoImplTest extends AbstractTest {
    @Autowired
    private RoleDao roleDao;
    private Role role;
    private Role actual;

    @BeforeAll
    void beforeAll() {
        role = new Role(UserRoleName.USER);
        actual = roleDao.save(role);
    }

    @Test
    void saveToDb_givenValidData_thenSuccess() {
        assertNotNull(actual);
        assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getFromDb_givenDuplicatedData_thenFail() {
        assertThrows(DataProcessingException.class, () -> roleDao.save(role),
                "Expected DataProcessingException when saving Role which already exist in DB");
    }

    @Test
    void getRoleByNameFromDb_givenValidData_thenSuccess() {
        Role roleFromDb = roleDao.getRoleByName(role.getRoleName().name()).orElse(null);
        assertNotNull(roleFromDb);
        assertEquals(roleFromDb.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_givenWrongData_thenFail() {
        Optional<Role> roleFromDb = roleDao.getRoleByName(UserRoleName.ADMIN.name());
        assertEquals(Optional.empty(), roleFromDb);
    }

    @AfterAll
    void afterAll() {
        roleDao.delete(role.getId());
    }
}
