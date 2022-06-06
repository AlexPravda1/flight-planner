package planner.controller;

import static java.util.stream.Collectors.toList;
import static planner.util.AirlineUtil.getVlzAirline;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dozer.Mapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import planner.model.Aircraft;
import planner.model.User;
import planner.model.dto.response.AircraftResponseDto;
import planner.model.json.flight.list.FlightList;
import planner.service.AircraftService;
import planner.service.UserService;
import planner.util.LeonCacheUtil;

@Controller
@RequiredArgsConstructor
@Log4j2
public class TestJspController {
    private final UserService userService;
    private final AircraftService aircraftService;
    private final Mapper entityMapper;
    private final LeonCacheUtil leonCacheUtil;

    @GetMapping("/welcome")
    public String welcome(Authentication authentication,
                          @RequestParam(name = "defaultName", required = false,
                                  defaultValue = "Traveller")
            String defaultName, Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("userName", authentication != null
                ? authentication.getName() : defaultName);

        LocalDateTime time = LocalDateTime.now();
        model.addAttribute("time", time);

        List<AircraftResponseDto> activeAircraftList =
                aircraftService.findAllActiveByAirline(getVlzAirline().getName())
                .stream()
                .map(acft -> entityMapper.map(acft, AircraftResponseDto.class))
                .collect(toList());

        model.addAttribute("acftList", activeAircraftList);
        log.info("/welcome page called");
        return "welcome";
    }

    @SuppressWarnings(value = "OptionalGetWithoutIsPresent")
    @GetMapping("/flights")
    public String flights(Authentication authentication,
            @RequestParam(name = "registration", required = false) String registration,
            @RequestParam(name = "daysRange", required = false, defaultValue = "0") long daysRange,
            @RequestParam(name = "hasNotes", required = false, defaultValue = "false")
            boolean hasNotes,
            @RequestParam(name = "hasFiles", required = false, defaultValue = "false")
            boolean hasFiles,
            Model model) {

        User user = userService.findByEmail(authentication.getName()).get();

        if (hasNotes) {
            List<FlightList> leonData = leonCacheUtil.getAllFlightsMap(user.getAirlineId())
                    .stream()
                    .filter((flight -> !flight.getNotes().getOps().isBlank()
                            || !flight.getNotes().getSales().isBlank()
                            || !flight.getTrip().getNotes().isBlank()
                            || flight.getTrip().getTripNotes() != null
                            && !flight.getTrip().getTripNotes()
                            .getTripSupplementaryInfo().isEmpty()))
                    .collect(toList());
            model.addAttribute("leonData", leonData);
            return "flights";
        }

        if (hasFiles) {
            List<FlightList> leonData = leonCacheUtil.getAllFlightsMap(user.getAirlineId())
                    .stream()
                    .filter(flight -> flight.getChecklist().getAllItems()
                            .stream().anyMatch(files -> !files.getFiles().isEmpty()))
                    .collect(toList());
            model.addAttribute("leonData", leonData);
            return "flights";
        }

        if (registration != null) {

            Aircraft aircraft = aircraftService.findByRegistration(registration);
            List<FlightList> leonData = leonCacheUtil.getAllFlightsMap(user.getAirlineId())
                    .stream().filter(acft -> acft.getAcft().getRegistration()
                            .equals(aircraft.getRegistration()))
                    .collect(toList());
            model.addAttribute("leonData", leonData);
        } else {
            List<FlightList> leonData = leonCacheUtil.getAllFlightsMap(user.getAirlineId());
            model.addAttribute("leonData", leonData);
        }
        log.debug(String.format(
                "\"FLIGHTS\" page called for: %s withing daysRange: %s and Registration: %s",
                getVlzAirline().getName(), daysRange, registration));
        return "flights";
    }
}
