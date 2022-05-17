package planner.service.impl;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import planner.AbstractTest;
import planner.dao.RoleDao;
import planner.model.Role;
import planner.model.UserRoleName;
import planner.service.RoleService;

class RoleServiceImplTest extends AbstractTest {
    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RoleService roleService;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role(UserRoleName.USER);
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
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


