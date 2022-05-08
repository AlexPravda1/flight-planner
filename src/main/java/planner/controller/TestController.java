package planner.controller;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
public class TestController {
    private final RoleService roleService;
    private final UserService userService;

    @GetMapping("/inject")
    public String injectData() {
        List<Role> roles = roleService.findAll();
        return "Injection must have been done already by @PostConstruct. "
                + "Roles list must have at least 2 roles: "
                + roles.stream()
                .map(n -> n.getRoleName().name())
                .collect(Collectors.toList());
    }

    @GetMapping("/message")
    public String getTest() {
        return "This is test String message only";
    }

    @GetMapping("/users")
    public List<String> getUsers() {
        return userService.findAll().stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
    }

    @GetMapping("/users/by-email")
    public UserResponseDto getUserByEmail(@RequestParam String email) {
        User user = userService.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(
                "User with email " + email + " not found in DB"));
        return MapperUtil.map(user, UserResponseDto.class);
    }
}
