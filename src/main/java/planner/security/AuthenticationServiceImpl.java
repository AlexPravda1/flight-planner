package planner.security;

import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import planner.exception.AuthenticationException;
import planner.model.User;
import planner.service.RoleService;
import planner.service.UserService;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User register(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(Set.of(roleService.getRoleByName("USER")));
        user = userService.save(user);
        return user;
    }

    @Override
    public User login(String login, String password) throws AuthenticationException {
        Optional<User> user = userService.findByEmail(login);
        String encodedPassword = passwordEncoder.encode(password);
        if (user.isEmpty() || user.get().getPassword().equals(encodedPassword)) {
            throw new AuthenticationException("Incorrect username or password!");
        }
        return user.get();
    }
}
