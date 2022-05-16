package planner.security;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import planner.exception.AuthenticationException;
import planner.model.Role;
import planner.model.User;
import planner.model.UserRoleName;
import planner.service.RoleService;
import planner.service.UserService;

class AuthenticationServiceImplTest {
    private String email;
    private String password;
    private String name;
    private String surname;
    private AuthenticationService authenticationService;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private RoleService roleService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        roleService = Mockito.mock(RoleService.class);
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService,
                passwordEncoder);
        email = "user@gmail.com";
        name = "John";
        surname = "Terris";
        password = "12345";
        user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(Set.of(new Role(UserRoleName.USER)));
    }

    @Test
    void register_validUserData_thenCorrect() {
        Mockito.when(userService.save(any())).thenReturn(user);
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(new Role(UserRoleName.USER));
        User actual = authenticationService.register(email, password, name, surname);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(email, actual.getEmail());
        Assertions.assertEquals(password, actual.getPassword());
    }

    @Test
    void login_validUserData_thenCorrect() throws AuthenticationException {
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        User actual = authenticationService.login(email, password);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(email, actual.getEmail());
        Assertions.assertEquals(password, actual.getPassword());
    }

    @Test
    void login_nonExistentUser_thenException() {
        Mockito.when(userService.findByEmail(email))
                .thenReturn(Optional.of(new User()));
        Assertions.assertThrows(AuthenticationException.class, () ->
                        authenticationService.login(email, password),
                "Expected AuthenticationException for nonExistent User");
    }
}
