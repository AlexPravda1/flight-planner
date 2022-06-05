package planner.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class IndexController {
    @GetMapping("/index")
    public String index(Authentication authentication, Model model) {
        model.addAttribute("userName", authentication.getName());
        log.debug("/index page controller called");
        return "index";
    }
}
