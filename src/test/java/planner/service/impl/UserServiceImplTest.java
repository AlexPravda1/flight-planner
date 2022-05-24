package planner.service.impl;

import static model.UserHardcoded.getUserNoRolesNoId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import planner.AbstractTest;
import planner.dao.UserDao;
import planner.model.User;

class UserServiceImplTest extends AbstractTest {
    private static final User expected = getUserNoRolesNoId();
    private User actualFromDb;
    @Mock
    private UserDao userDao;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void save_validData_thenCorrect() {
        when(userDao.save(expected)).thenReturn(expected);
        when(passwordEncoder.encode(any())).thenReturn(expected.getPassword());
        actualFromDb = userService.save(expected);
        validateUser(actualFromDb);

    }

    @Test
    void findById_validData_thenCorrect() {
        when(userDao.findById(any())).thenReturn(Optional.of(expected));
        actualFromDb = userService.findById(expected.getId()).orElse(null);
        validateUser(actualFromDb);
    }

    @Test
    void findByEmail_validData_thenCorrect() {
        when(userDao.findByEmail(expected.getEmail())).thenReturn(Optional.of(expected));
        User actualFromDb = userService.findByEmail(expected.getEmail()).orElse(null);
        validateUser(actualFromDb);
    }

    private void validateUser(User actualFromDb) {
        assertNotNull(actualFromDb);
        assertEquals(expected.getEmail(), actualFromDb.getEmail());
        assertEquals(expected.getName(), actualFromDb.getName());
        assertEquals(expected.getSurname(), actualFromDb.getSurname());
    }
}
