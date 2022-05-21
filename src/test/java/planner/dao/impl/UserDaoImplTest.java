package planner.dao.impl;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static planner.model.UserRoleName.USER;

import java.util.Optional;
import java.util.Set;
import model.hardcoded.UserTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import planner.AbstractTest;
import planner.dao.RoleDao;
import planner.dao.UserDao;
import planner.exception.DataProcessingException;
import planner.model.Role;
import planner.model.User;

class UserDaoImplTest extends AbstractTest {
    private User expected;
    private User actualFromDb;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        expected = UserTest.getUserNoRolesNoId();
    }

    @AfterEach
    void tearDown() {
        if (actualFromDb != null) {
            userDao.delete(actualFromDb.getId());
        }
        roleDao.getRoleByName(USER.value()).ifPresent(role -> roleDao.delete(role.getId()));
    }

    @Test
    void saveUserToDb_givenValidUser_thenSuccess() {
        actualFromDb = userDao.save(expected);
        validateUser(expected, actualFromDb);
    }

    @Test
    void saveUserToDb_givenDuplicatedUser_thenFail() {
        actualFromDb = userDao.save(expected);
        assertThrows(DataProcessingException.class, () -> userDao.save(expected),
                "Expected DataProcessingException when saving User which already exist in DB");
    }

    @Test
    void findUserByEmailFromDb_givenValidEmail_thenSuccess() {
        expected.setRoles(Set.of(roleDao.save(new Role(USER))));
        userDao.save(expected);
        actualFromDb = userDao.findByEmail(expected.getEmail()).orElse(null);
        validateUser(expected, actualFromDb);
    }

    @Test
    void findUserByEmailFromDb_givenWrongEmail_thenFail() {
        assertEquals(Optional.empty(), userDao.findByEmail("prefix" + expected.getEmail()),
                "should be empty Optional");
        assertEquals(Optional.empty(), userDao.findByEmail(SPACE),
                "should be empty Optional");
    }

    private void validateUser(User expected, User actual) {
        assertNotNull(actual);
        assertEquals(expected.getEmail(), actual.getEmail(),
                "should have the same email");
        assertEquals(expected.getName(), actual.getName(),
                "should have the same name");
        assertEquals(expected.getSurname(), actual.getSurname(),
                "should have the same surname");
    }
}
