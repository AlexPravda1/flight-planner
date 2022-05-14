package planner.config;

import static planner.config.template.UserRoleName.ADMIN;
import static planner.config.template.UserRoleName.USER;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import planner.model.Role;
import planner.model.User;
import planner.service.RoleService;
import planner.service.UserService;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final RoleService roleService;
    private final UserService userService;

    @PostConstruct
    public void inject() {
        List<Role> roles = roleService.findAll();

        if (roles.isEmpty()) {
            roleService.save(new Role(ADMIN));
            roleService.save(new Role(USER));
            roles = roleService.findAll();

            User bobAdmin = new User();
            bobAdmin.setRoles(new HashSet<>(roles));
            bobAdmin.setEmail("bob@i.ua");
            bobAdmin.setPassword("1234");
            bobAdmin.setName("bob");
            bobAdmin.setSurname("bobinsky");
            userService.save(bobAdmin);

            User aliceUser = new User();
            aliceUser.setRoles(Set.of(roles.get(1)));
            aliceUser.setEmail("alice@i.ua");
            aliceUser.setPassword("1234");
            aliceUser.setName("alice");
            aliceUser.setSurname("alicynsky");
            userService.save(aliceUser);
        }
    }
}
