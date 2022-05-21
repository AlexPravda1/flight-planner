package planner.dao.impl;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static planner.model.UserRoleName.ADMIN;
import static planner.model.UserRoleName.USER;

import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import planner.AbstractTest;
import planner.dao.RoleDao;
import planner.exception.DataProcessingException;
import planner.model.Role;

class RoleDaoImplTest extends AbstractTest {
    @Autowired
    private RoleDao roleDao;
    private Role expected;
    private Role actualFromDb;

    @BeforeEach
    void setUp() {
        expected = new Role(USER);
    }

    @Test
    void saveRoleToDb_givenValidRole_thenSuccess() {
        actualFromDb = roleDao.save(expected);
        validateRoles(expected, actualFromDb);
    }

    @Test
    void saveRoleToDb_givenDuplicatedRole_thenFail() {
        roleDao.save(expected);
        assertThrows(DataProcessingException.class, () -> roleDao.save(expected),
                "Expected DataProcessingException when saving Role which already exist in DB");
    }

    @Test
    void getRoleByNameFromDb_givenExistingRole_thenSuccess() {
        roleDao.save(expected);
        actualFromDb = roleDao.getRoleByName(expected.getRoleName().name()).orElse(null);
        validateRoles(expected, actualFromDb);
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

    @AfterEach
    void tearDown() {
        roleDao.getRoleByName(expected.getRoleName().name())
                .ifPresent(role -> roleDao.delete(role.getId()));
    }
}
