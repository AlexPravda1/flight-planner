package planner.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import planner.model.User;
import planner.model.json.flight.list.FlightList;
import planner.service.LeonFlightListService;
import planner.service.UserService;
import planner.util.LeonCacheUtil;

@Controller
@RequiredArgsConstructor
@Log4j2
public class FlightsController {
    private final LeonFlightListService leonFlightListService;
    private final UserService userService;
    private final LeonCacheUtil leonCacheUtil;

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
        List<FlightList> allFlightsList = leonCacheUtil.getAllFlightsMap(user.getAirlineId());

        allFlightsList = leonFlightListService.getFlightsHasRegistration(
                allFlightsList, registration);
        allFlightsList = leonFlightListService.getFlightsHasDaysRange(allFlightsList, daysRange);
        allFlightsList = leonFlightListService.getFlightsHasNotes(allFlightsList, hasNotes);
        allFlightsList = leonFlightListService.getFlightsHasFiles(allFlightsList, hasFiles);
        log.debug("/flights processed for airlineId {}, registration {}, "
                        + "daysRange: {}, withNotes: {}, withFiles: {}",
                user.getAirlineId(), registration, daysRange, hasNotes, hasFiles);

        model.addAttribute("leonData", allFlightsList);
        return "flights";
    }
}
