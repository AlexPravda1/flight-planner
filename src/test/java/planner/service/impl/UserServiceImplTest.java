package planner.service.impl;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import planner.dao.UserDao;
import planner.model.User;
import planner.service.UserService;

class UserServiceImplTest {
    private String userEmail;
    private String userPassword;
    private Long userId;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private User user;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        userEmail = "user@gmail.com";
        userPassword = "12345";
        userId = 1L;
        user = new User();
        user.setId(userId);
        user.setPassword(userPassword);
        user.setEmail(userEmail);
    }

    @Test
    void save_validData_thenCorrect() {
        Mockito.when(userDao.save(user)).thenReturn(user);
        Mockito.when(passwordEncoder.encode(any())).thenReturn(user.getPassword());
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userEmail, actual.getEmail());
        Assertions.assertEquals(userPassword, actual.getPassword());
        Assertions.assertEquals(userId, actual.getId());
    }

    @Test
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    void findById_validData_thenCorrect() {
        Mockito.when(userDao.findById(any())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(userId);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getClass(), actual.get().getClass());
    }

    @Test
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    void findByEmail_validData_thenCorrect() {
        Mockito.when(userDao.findByEmail(userEmail)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(userEmail);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getClass(), actual.get().getClass());
        Assertions.assertEquals(user.getEmail(), actual.get().getEmail());
    }
}
