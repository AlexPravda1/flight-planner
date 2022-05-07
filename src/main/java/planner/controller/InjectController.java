package planner.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import planner.model.Role;
import planner.model.User;
import planner.service.RoleService;
import planner.service.UserService;

@RequiredArgsConstructor
@RestController
public class InjectController {
    private final RoleService roleService;
    private final UserService userService;

    @GetMapping("/inject")
    public String injectData() {
        List<Role> roles = roleService.findAll();
        if (!roles.isEmpty()) {
            return "Injection complete";
        }
        // Save roles
        roleService.save(new Role(Role.RoleName.ADMIN));
        roleService.save(new Role(Role.RoleName.USER));
        roles = roleService.findAll();

        // save users
        User bobAdmin = new User();
        bobAdmin.setRoles(new HashSet<>(roles));
        bobAdmin.setEmail("bob@i.ua");
        bobAdmin.setPassword("1234");
        bobAdmin.setName("bob");
        bobAdmin.setSurname("bobinsky");
        userService.save(bobAdmin);

        User aliceUser = new User();
        aliceUser.setRoles(Set.of(roles.get(0)));
        aliceUser.setEmail("alice@i.ua");
        aliceUser.setPassword("1234");
        aliceUser.setName("alice");
        aliceUser.setSurname("alicynsky");
        userService.save(aliceUser);

        return roles + "*|*|*|*|*|*|**|*|*"
                + bobAdmin + "*|*|*|*|*|*|**|*|*"
                + aliceUser + "*|*|*|*|*|*|**|*|*"
                + "Done!";
    }

    @GetMapping("/test")
    public String getTest() {
        return "This is test page";
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        User bob = userService.findByEmail("bob@i.ua").get();
        User alice = userService.findByEmail("alice@i.ua").get();
        return List.of(bob, alice);
    }
}
