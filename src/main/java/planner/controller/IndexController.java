package planner.controller;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class IndexController {
    @GetMapping("/index")
    public String hello(Authentication authentication, HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        log.debug("/index controller called");
        return String.format("Hello, %s!", authentication.getName()
                + ". Your access level: " + authentication.getAuthorities()
                + ". Your session id is: " + sessionId);
    }
}
