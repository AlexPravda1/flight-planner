package planner.service.impl;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import planner.AbstractTest;
import planner.dao.RoleDao;
import planner.model.Role;
import planner.model.UserRoleName;

class RoleServiceImplTest extends AbstractTest {
    @Mock
    private RoleDao roleDao;

    @InjectMocks
    private RoleServiceImpl roleService;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role(UserRoleName.USER);
    }

    @Test
    void save_validData_thenCorrect() {
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_validData_thenCorrect() {
        Mockito.when(roleDao.getRoleByName(UserRoleName.USER.name()))
                .thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(role.getRoleName().name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }
}


