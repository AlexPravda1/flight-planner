package planner.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import planner.model.Role;
import planner.model.User;
import planner.model.dto.response.UserResponseDto;
import planner.service.RoleService;
import planner.service.UserService;
import planner.util.MapperUtil;

@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class InjectController {
    private final RoleService roleService;
    private final UserService userService;

    @GetMapping("/inject")
    public String injectData() {
        List<Role> roles = roleService.findAll();
        return "Injection must have been done already by @PostConstruct. "
                + "Roles list must have at least 2 roles: "
                + roles.toString();
    }

    @GetMapping("/message")
    public String getTest() {
        return "This is test String message only";
    }

    @GetMapping("/users")
    public List<UserResponseDto> getUsers() {
        User bob = userService.findByEmail("bob@i.ua").get();
        User alice = userService.findByEmail("alice@i.ua").get();
        List<User> users = List.of(bob, alice);
        return MapperUtil.getDtoList(users, UserResponseDto.class);
    }

    @GetMapping("/users/bob")
    public UserResponseDto getUserBob() {
        User bob = userService.findByEmail("bob@i.ua").get();
        return MapperUtil.getDtoEntity(bob, UserResponseDto.class);
    }
}
