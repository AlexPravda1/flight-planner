package planner.security.jwt;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static planner.model.UserRoleName.USER;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import model.hardcoded.UserTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;
import planner.AbstractTest;
import planner.exception.InvalidJwtAuthenticationException;

class JwtTokenProviderTest extends AbstractTest {
    private static planner.model.User user;
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGdtYW"
            + "lsLmNvbSIsInJvbGVzIjpbIlVTRVIiXSwiaWF0IjoxNjUyNzA4ODgxLCJleHAiOjE2NTQ4NTYzNjV"
            + "9.CT7cPAcYsbGxfPXJYNvflYFoyAmBE49f3KJ37xx8a7I";
    @Mock
    private UserDetailsService userDetailsService;
    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secretTestKey");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", Integer.MAX_VALUE);
    }

    @BeforeAll
    static void beforeAll() {
        user = UserTest.getUserNoRolesNoId();
    }

    @Test
    void createToken_givenValidData_thenSuccess() {
        List<String> roles = List.of(USER.value());
        String actual = jwtTokenProvider.createToken(user.getEmail(), roles);
        assertNotNull(actual);
        assertEquals(VALID_TOKEN.length(), actual.length());
    }

    @Test
    void getAuthentication_givenValidData_thenSuccess() {
        UserDetails userDetails = org.springframework.security.core.userdetails
                .User.withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(List.of(USER.value()).toArray(String[]::new))
                .build();
        when(userDetailsService.loadUserByUsername(any())).thenReturn(userDetails);
        Authentication authentication = jwtTokenProvider.getAuthentication(VALID_TOKEN);
        User actual = (User) authentication.getPrincipal();
        assertNotNull(actual);
        assertEquals(actual.getUsername(), user.getEmail());
    }

    @Test
    void getUsername_givenValidData_thenSuccess() {
        String actual = jwtTokenProvider.getUsername(VALID_TOKEN);
        assertNotNull(actual);
        assertEquals(actual, user.getEmail());
    }

    @Test
    void resolveToken_givenValidData_thenSuccess() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getHeader("Authorization")).thenReturn("Bearer " + VALID_TOKEN);
        String actual = jwtTokenProvider.resolveToken(req);
        assertNotNull(actual);
        assertEquals(actual, VALID_TOKEN);
    }

    @Test
    void validateToken_givenValidToken_thenSuccess() {
        assertTrue(jwtTokenProvider.validateToken(VALID_TOKEN));
    }

    @Test
    void validateToken_givenWrongToken_thenFail() {
        assertThrows(InvalidJwtAuthenticationException.class,
                () -> jwtTokenProvider.validateToken(SPACE),
                "Expected RuntimeException for invalid Token");
    }
}
