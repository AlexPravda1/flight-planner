package planner.security;

import static planner.model.UserRoleName.USER;

import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import planner.exception.AuthenticationException;
import planner.model.User;
import planner.service.RoleService;
import planner.service.UserService;

@RequiredArgsConstructor
@Service
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User register(String email, String password, String name, String surname) {
        User user = new User();
        user.setRoles(Set.of(roleService.getRoleByName(USER.value())));
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setSurname(surname);
        user = userService.save(user);
        log.debug(String.format("AuthenticationServiceImpl registered user: %s with ID: %s",
                user.getEmail(), user.getId()));
        return user;
    }

    @Override
    public User login(String login, String password) throws AuthenticationException {
        Optional<User> user = userService.findByEmail(login);
        if (user.isEmpty() || !passwordEncoder.matches(password, user.get().getPassword())) {
            log.debug("AuthenticationException is thrown, Incorrect username or password");
            throw new AuthenticationException("Incorrect username or password!!!");
        }
        log.debug(String.format("%s user login: SUCCESS from AuthenticationServiceImpl",
                user.get().getEmail()));
        return user.get();
    }
}
