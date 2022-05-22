package planner.service.impl;

import static org.mockito.Mockito.when;
import static planner.model.UserRoleName.USER;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import planner.AbstractTest;
import planner.dao.RoleDao;
import planner.model.Role;

class RoleServiceImplTest extends AbstractTest {
    private static Role role;
    @Mock
    private RoleDao roleDao;

    @InjectMocks
    private RoleServiceImpl roleService;

    @BeforeAll
    static void beforeAll() {
        role = new Role(USER);
    }

    @Test
    void saveRole_givenValidData_thenSuccess() {
        when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        validateRole(actual);
    }

    @Test
    void getRoleByName_givenValidData_thenSuccess() {
        when(roleDao.getRoleByName(USER.name()))
                .thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(role.getRoleName().name());
        validateRole(actual);
    }

    private void validateRole(Role actual) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }
}


