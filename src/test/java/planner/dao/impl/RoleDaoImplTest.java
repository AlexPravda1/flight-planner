package planner.dao.impl;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static planner.model.UserRoleName.ADMIN;
import static planner.model.UserRoleName.USER;

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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoleDaoImplTest extends AbstractTest {
    @Autowired
    private RoleDao roleDao;
    private Role expectedRole;
    private Role roleFromDb;

    @BeforeAll
    void beforeAll() {
        expectedRole = new Role(USER);
        roleFromDb = roleDao.save(expectedRole);
    }

    @Test
    void saveRoleToDb_givenValidRole_thenSuccess() {
        validateRoles(expectedRole, roleFromDb);
    }

    @Test
    void saveRoleToDb_givenDuplicatedRole_thenFail() {
        assertThrows(DataProcessingException.class, () -> roleDao.save(expectedRole),
                "Expected DataProcessingException when saving Role which already exist in DB");
    }

    @Test
    void getRoleByNameFromDb_givenExistingRole_thenSuccess() {
        Role actual = roleDao.getRoleByName(expectedRole.getRoleName().name()).orElse(null);
        validateRoles(expectedRole, actual);
    }

    @Test
    void getRoleByName_givenWrongRole_thenFail() {
        assertEquals(Optional.empty(), roleDao.getRoleByName(ADMIN.value()),
                "should be empty Optional");
    }

    @Test
    void getRoleByName_givenWrongDataType_thenFail() {
        assertThrows(DataProcessingException.class, () -> roleDao.getRoleByName(SPACE),
                "Expected DataProcessingException when non-Role data passed");
    }

    private void validateRoles(Role expectedRole, Role actualRole) {
        assertNotNull(actualRole);
        assertEquals(expectedRole.getRoleName(), actualRole.getRoleName(),
                "should have the same name");
    }

    @AfterAll
    void afterAll() {
        roleDao.delete(roleFromDb.getId());
    }
}
