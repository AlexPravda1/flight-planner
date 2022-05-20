package planner.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import planner.AbstractTest;
import planner.dao.RoleDao;
import planner.dao.UserDao;
import planner.exception.DataProcessingException;
import planner.model.Role;
import planner.model.User;
import planner.model.UserRoleName;

class UserDaoImplTest extends AbstractTest {
    private String userEmail;
    private String name;
    private String surname;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    private User user;

    @BeforeEach
    void setUp() {
        userEmail = "user@gmail.com";
        name = "John";
        surname = "Terris";
        String userPassword = "12345";
        user = new User();
        user.setPassword(userPassword);
        user.setEmail(userEmail);
        user.setName(name);
        user.setSurname(surname);
    }

    @Test
    void save_validUserData_thenCorrect() {
        //Long userId = 1L;
        User actual = userDao.save(user);
        assertNotNull(actual);
        //assertEquals(userId, actual.getId());
        assertEquals(userEmail, actual.getEmail());
        assertEquals(name, actual.getName());
        assertEquals(surname, actual.getSurname());
        userDao.delete(actual.getId());
    }

    @Test
    void save_duplicatedUser_thenException() {
        User actual = userDao.save(user);
        assertNotNull(actual);
        assertThrows(DataProcessingException.class, () -> userDao.save(user),
                "Expected DataProcessingException when saving User which already exist in DB");
        userDao.delete(actual.getId());
    }

    @Test
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    void findByEmail_validUserData_thenCorrect() {
        Role roleFromDb = roleDao.save(new Role(UserRoleName.USER));
        user.setRoles(Set.of(roleFromDb));
        User userFromDb = userDao.save(user);
        String userEmail = "user@gmail.com";
        Optional<User> actual = userDao.findByEmail(userEmail);
        assertNotNull(actual);
        assertEquals(userFromDb.getId(), actual.get().getId());
        assertEquals(userFromDb.getEmail(), actual.get().getEmail());
        assertEquals(userFromDb.getRoles().size(), actual.get().getRoles().size());
        userDao.delete(userFromDb.getId());
        roleDao.delete(roleFromDb.getId());
    }

    @Test
    void findByEmail_nonExistentUser_thenEmptyOptional() {
        User userFromDb = userDao.save(user);
        String userEmail = "prefix" + userFromDb.getEmail();
        Optional<User> actual = userDao.findByEmail(userEmail);
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
        userDao.delete(userFromDb.getId());
    }
}
