package planner.security.jwt;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;
import planner.AbstractTest;

class JwtTokenProviderTest extends AbstractTest {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGdtYW"
            + "lsLmNvbSIsInJvbGVzIjpbIlVTRVIiXSwiaWF0IjoxNjUyNzA4ODgxLCJleHAiOjE2NTQ4NTYzNjV"
            + "9.CT7cPAcYsbGxfPXJYNvflYFoyAmBE49f3KJ37xx8a7I";
    private String userEmail;
    private String userPassword;
    @Mock
    private UserDetailsService userDetailsService;
    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        userEmail = "user@gmail.com";
        userPassword = "12345";
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secretTestKey");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", Integer.MAX_VALUE);
    }

    @Test
    void createToken_validData_thenCorrect() {
        List<String> roles = List.of("USER");
        String actual = jwtTokenProvider.createToken(userEmail, roles);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(VALID_TOKEN.length(), actual.length());
    }

    @Test
    void getAuthentication_validData_thenCorrect() {
        User.UserBuilder builder = org.springframework.security.core.userdetails
                .User.withUsername(userEmail)
                .password(userPassword)
                .roles(List.of("USER").toArray(String[]::new));
        UserDetails userDetails = builder.build();
        Mockito.when(this.userDetailsService.loadUserByUsername(any())).thenReturn(userDetails);
        Authentication authentication = jwtTokenProvider.getAuthentication(VALID_TOKEN);
        User actual = (User) authentication.getPrincipal();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getUsername(), userEmail);
    }

    @Test
    void getUsername_validData_thenCorrect() {
        String actual = jwtTokenProvider.getUsername(VALID_TOKEN);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, userEmail);
    }

    @Test
    void resolveToken_validData_thenCorrect() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer " + VALID_TOKEN);
        String actual = jwtTokenProvider.resolveToken(req);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, VALID_TOKEN);
    }

    @Test
    void validateToken_validToken_thenCorrect() {
        boolean actual = jwtTokenProvider.validateToken(VALID_TOKEN);
        Assertions.assertTrue(actual);
    }

    @Test
    void validateToken_invalidToken_thenException() {
        String invalidToken = "part1.part2.token";
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(invalidToken),
                "Expected RuntimeException for invalid Token");
    }
}
