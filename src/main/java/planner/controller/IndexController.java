package planner.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import planner.service.UserService;

@Controller
@Log4j2
@RequiredArgsConstructor
@SuppressWarnings(value = "OptionalGetWithoutIsPresent")
public class IndexController {
    private final UserService userService;

    @GetMapping("/index")
    public String index(Authentication authentication, Model model) {
        String userName = userService.findByEmail(authentication.getName()).get().getName();
        model.addAttribute("userName", userName);
        log.debug("/index page controller called");
        return "index";
    }
}
