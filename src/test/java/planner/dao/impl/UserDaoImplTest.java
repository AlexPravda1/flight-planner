package planner.dao.impl;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static planner.model.UserRoleName.USER;

import java.util.Optional;
import java.util.Set;
import model.hardcoded.UserTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import planner.AbstractTest;
import planner.dao.RoleDao;
import planner.dao.UserDao;
import planner.exception.DataProcessingException;
import planner.model.Role;
import planner.model.User;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDaoImplTest extends AbstractTest {
    private User expected;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;

    @BeforeAll
    void beforeAll() {
        expected = new UserTest().getUserNoRolesNoId();
        expected.setRoles(Set.of(roleDao.save(new Role(USER))));
    }

    @Test
    void saveUserToDb_givenValidUser_thenSuccess() {
        User actual = userDao.save(expected);
        validateUsers(expected, actual);
    }

    @Test
    void saveUserToDb_givenDuplicatedUser_thenFail() {
        assertThrows(DataProcessingException.class, () -> userDao.save(expected),
                "Expected DataProcessingException when saving User which already exist in DB");
    }

    @Test
    void findUserByEmailFromDb_givenValidEmail_thenSuccess() {
        User actual = userDao.findByEmail(expected.getEmail()).orElse(null);
        validateUsers(expected, actual);
    }

    @Test
    void findUserByEmailFromDb_givenWrongEmail_thenFail() {
        assertEquals(Optional.empty(), userDao.findByEmail("invalid@gmail.com"),
                "should be empty Optional");
        assertEquals(Optional.empty(), userDao.findByEmail(SPACE),
                "should be empty Optional");
    }

    private void validateUsers(User expected, User actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId(),
                "should have the same id");
        assertEquals(expected.getEmail(), actual.getEmail(),
                "should have the same email");
        assertEquals(expected.getName(), actual.getName(),
                "should have the same name");
        assertEquals(expected.getSurname(), actual.getSurname(),
                "should have the same surname");
        assertEquals(expected.getRoles().size(), actual.getRoles().size(),
                "should have the same amount of Roles");
    }

    @AfterAll
    void afterAll() {
        userDao.delete(expected.getId());
        roleDao.delete(1L);
    }
}
