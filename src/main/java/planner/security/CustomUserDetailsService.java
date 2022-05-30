package planner.security;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import planner.model.User;
import planner.service.UserService;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userService.findByEmail(email);
        log.debug(String.format("CustomUserDetailsService provided %s user based on user email",
                userOptional.isPresent() ? "VALID" : "EMPTY"));
        UserBuilder builder;
        if (userOptional.isPresent()) {
            builder = org.springframework.security.core.userdetails.User.withUsername(email);
            builder.password(userOptional.get().getPassword());
            builder.roles(userOptional.get().getRoles()
                    .stream()
                    .map(r -> r.getRoleName().name())
                    .toArray(String[]::new));

            return builder.build();
        }
        throw new UsernameNotFoundException("User with login: " + email + " not found.");
    }
}
