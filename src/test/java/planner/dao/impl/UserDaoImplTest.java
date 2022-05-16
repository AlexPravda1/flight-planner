package planner.dao.impl;

import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    private UserDao userDao;
    private RoleDao roleDao;
    private User user;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class, UserRoleName.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
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
        Long userId = 1L;
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userId, actual.getId());
        Assertions.assertEquals(userEmail, actual.getEmail());
        Assertions.assertEquals(name, actual.getName());
        Assertions.assertEquals(surname, actual.getSurname());
    }

    @Test
    void save_duplicatedUser_thenException() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertThrows(DataProcessingException.class, () -> userDao.save(user),
                "Expected DataProcessingException when saving User which already exist in DB");
    }

    @Test
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    void findByEmail_validUserData_thenCorrect() {
        Role roleFromDb = roleDao.save(new Role(UserRoleName.USER));
        user.setRoles(Set.of(roleFromDb));
        User userFromDb = userDao.save(user);
        String userEmail = "user@gmail.com";
        Optional<User> actual = userDao.findByEmail(userEmail);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userFromDb.getId(), actual.get().getId());
        Assertions.assertEquals(userFromDb.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(userFromDb.getRoles().size(), actual.get().getRoles().size());
    }

    @Test
    void findByEmail_nonExistentUser_thenEmptyOptional() {
        User userFromDb = userDao.save(user);
        String userEmail = "prefix" + userFromDb.getEmail();
        Optional<User> actual = userDao.findByEmail(userEmail);
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.isEmpty());
    }
}
