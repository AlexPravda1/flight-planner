package planner.controller;

import static java.util.stream.Collectors.toList;
import static planner.util.AirlineUtil.getVlzAirline;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dozer.Mapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import planner.model.User;
import planner.model.dto.response.AircraftResponseDto;
import planner.service.AircraftService;
import planner.service.UserService;

@Controller
@RequiredArgsConstructor
@Log4j2
public class TestJspController {
    private final UserService userService;
    private final AircraftService aircraftService;
    private final Mapper entityMapper;

    @GetMapping("/welcome")
    public String welcome(
            @RequestParam(name = "name", required = false, defaultValue = "Traveller")
            String name, Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("userName", name);

        LocalDateTime time = LocalDateTime.now();
        model.addAttribute("time", time);

        List<AircraftResponseDto> activeAircraftList =
                aircraftService.findAllActiveByAirline(getVlzAirline().getName())
                .stream()
                .map(acft -> entityMapper.map(acft, AircraftResponseDto.class))
                .collect(toList());

        model.addAttribute("acftList", activeAircraftList);
        log.info("processing welcome page");
        return "welcome";
    }
}
