package planner.controller;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dozer.Mapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import planner.model.dto.response.AircraftResponseDto;
import planner.model.dto.response.UserResponseDto;
import planner.service.AircraftService;
import planner.service.UserService;

@Controller
@Log4j2
@RequiredArgsConstructor
public class IndexController {
    private final UserService userService;
    private final AircraftService aircraftService;
    private final Mapper entityMapper;

    @GetMapping("/")
    public String index(Authentication authentication, Model model) {
        UserResponseDto user = setUserToPage(authentication, model);
        setAircraftListToPage(model, user);
        log.debug("/index page controller called");
        return "index";
    }

    @GetMapping("/profile")
    public String welcome(Authentication authentication, Model model) {
        setTimeToPage(model);
        UserResponseDto user = setUserToPage(authentication, model);
        setAircraftListToPage(model, user);
        log.info("/profile page called");
        return "profile";
    }

    private void setTimeToPage(Model model) {
        LocalDateTime time = LocalDateTime.now();
        model.addAttribute("time", time);
    }

    private void setAircraftListToPage(Model model, UserResponseDto user) {
        List<AircraftResponseDto> activeAircraftList =
                aircraftService.findAllActiveByAirlineId(user.getAirlineId())
                        .stream()
                        .map(acft -> entityMapper.map(acft, AircraftResponseDto.class))
                        .collect(toList());
        model.addAttribute("acftList", activeAircraftList);
    }

    @SuppressWarnings(value = "OptionalGetWithoutIsPresent")
    private UserResponseDto setUserToPage(Authentication authentication, Model model) {
        UserResponseDto user = entityMapper.map(
                userService.findByEmail(authentication.getName()).get(), UserResponseDto.class);
        model.addAttribute("user", user);
        return user;
    }
}
