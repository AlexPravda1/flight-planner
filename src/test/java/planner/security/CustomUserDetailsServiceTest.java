package planner.security;

import static model.UserHardcoded.getUserWithUserRoleNoId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import planner.AbstractTest;
import planner.model.User;
import planner.service.UserService;

class CustomUserDetailsServiceTest extends AbstractTest {
    private static final User expected = getUserWithUserRoleNoId();
    @Mock
    private UserService userService;
    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    void loadUserByUsername_givenValidEmail_thenSuccess() {
        when(userService.findByEmail(expected.getEmail())).thenReturn(Optional.of(expected));
        UserDetails actual = userDetailsService.loadUserByUsername(expected.getEmail());
        validateUserDetails(actual);
    }

    @Test
    void loadUserByUsername_nonExistentUsername_thenFail() {
        when(userService.findByEmail(expected.getEmail())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(expected.getEmail()),
                "Expected UsernameNotFoundException when User email doesn't exist");
    }

    private void validateUserDetails(UserDetails actual) {
        assertEquals(expected.getEmail(), actual.getUsername(),
                "should have the same email");
        assertEquals(expected.getPassword(), actual.getPassword(),
                "should have the same password");
        assertEquals(expected.getRoles().size(), actual.getAuthorities().size(),
                "should have the same amount of User Roles");
    }
}
