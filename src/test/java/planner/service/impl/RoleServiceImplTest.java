package planner.service.impl;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import planner.dao.RoleDao;
import planner.model.Role;
import planner.model.UserRoleName;
import planner.service.RoleService;

class RoleServiceImplTest {
    private RoleDao roleDao;
    private RoleService roleService;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
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
