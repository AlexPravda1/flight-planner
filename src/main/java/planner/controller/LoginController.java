package planner.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
public class LoginController {

    @RequestMapping("/user-login")
    public String userLogin() {
        log.debug("/user_login page is called from LoginController");
        return "user-login";
    }
}
