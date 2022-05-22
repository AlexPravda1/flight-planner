package planner.security;

import static model.hardcoded.UserTest.getUserNoRolesNoId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static planner.model.UserRoleName.USER;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import planner.AbstractTest;
import planner.exception.AuthenticationException;
import planner.model.Role;
import planner.model.User;
import planner.service.RoleService;
import planner.service.UserService;

class AuthenticationServiceImplTest extends AbstractTest {
    private static User expected;
    @Mock
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleService roleService;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeAll
    static void beforeAll() {
        expected = getUserNoRolesNoId();
    }

    @Test
    void registerUser_givenValidUserData_thenSuccess() {
        when(userService.save(any())).thenReturn(expected);
        when(roleService.getRoleByName(any())).thenReturn(new Role(USER));
        User actual = authenticationService.register(expected.getEmail(),
                expected.getPassword(), expected.getName(), expected.getSurname());
        validateUser(actual);
    }

    @Test
    void loginUser_givenValidUserData_thenSuccess() throws AuthenticationException {
        when(userService.findByEmail(expected.getEmail())).thenReturn(Optional.of(expected));
        when(passwordEncoder.matches(
                expected.getPassword(), expected.getPassword())).thenReturn(true);
        User actual = authenticationService.login(expected.getEmail(), expected.getPassword());
        validateUser(actual);
    }

    @Test
    void loginUser_givenNonExistentUser_thenFail() {
        when(userService.findByEmail(any()))
                .thenReturn(Optional.of(new User()));
        Assertions.assertThrows(AuthenticationException.class, () ->
                        authenticationService.login(expected.getEmail(), expected.getPassword()),
                "Expected AuthenticationException for nonExistent User");
    }

    private void validateUser(User actual) {
        assertNotNull(actual);
        assertEquals(expected.getEmail(), actual.getEmail(),
                "should have the same email");
        assertEquals(expected.getName(), actual.getName(),
                "should have the same name");
        assertEquals(expected.getPassword(), actual.getPassword(),
                "should have the same password");
    }
}
