package planner.controller;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import planner.model.User;
import planner.service.UserService;

@Controller
@RequiredArgsConstructor
public class TestJspController {
    private final UserService userService;

    @GetMapping("/welcome")
    public String welcome(
            @RequestParam(name = "name", required = false, defaultValue = "Traveller")
            String name, Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("userName", name);

        LocalDateTime time = LocalDateTime.now();
        model.addAttribute("time", time);
        return "welcome";
    }
}
