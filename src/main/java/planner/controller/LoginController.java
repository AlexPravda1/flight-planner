package planner.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class LoginController {

    @GetMapping("/user_login")
    public String login() {
        log.debug("/login page is called from LoginController");
        return "user_login";
    }
}
