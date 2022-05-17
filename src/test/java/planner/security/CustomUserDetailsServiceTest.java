package planner.security;

import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import planner.AbstractTest;
import planner.model.Role;
import planner.model.User;
import planner.model.UserRoleName;
import planner.service.UserService;

class CustomUserDetailsServiceTest extends AbstractTest {
    private String userEmail;
    private String userPassword;
    @Mock
    private UserService userService;
    @InjectMocks
    private CustomUserDetailsService userDetailsService;
    private User user;

    @BeforeEach
    void setUp() {
        userEmail = "user@gmail.com";
        userPassword = "12345";
        user = new User();
        user.setEmail(userEmail);
        user.setPassword(userPassword);
        user.setRoles(Set.of(new Role(UserRoleName.USER)));
    }

    @Test
    void loadUserByUsername_validData_thenCorrect() {
        Mockito.when(userService.findByEmail(userEmail)).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(userEmail);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userEmail, actual.getUsername());
        Assertions.assertEquals(userPassword, actual.getPassword());
    }

    @Test
    void loadUserByUsername_nonExistentUsername_thenException() {
        Mockito.when(userService.findByEmail(userEmail)).thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(userEmail),
                "Expected UsernameNotFoundException when User email doesn't exist");
    }
}
