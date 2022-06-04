package planner.controller;

import static java.util.stream.Collectors.toList;
import static planner.util.AirlineUtil.getVlzAirline;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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
import planner.model.Airline;
import planner.model.User;
import planner.model.dto.response.AircraftResponseDto;
import planner.model.json.flight.list.FlightList;
import planner.model.json.root.LeonMetaData;
import planner.service.AircraftService;
import planner.service.AirlineService;
import planner.service.LeonApiService;
import planner.service.UserService;

@Controller
@RequiredArgsConstructor
@Log4j2
public class TestJspController {
    private final UserService userService;
    private final AircraftService aircraftService;
    private final AirlineService airlineService;
    private final LeonApiService leonApiService;
    private final Mapper entityMapper;
    private final ObjectMapper jsonMapper;

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
            Model model) throws JsonProcessingException {

        User user = userService.findByEmail(authentication.getName()).get();
        Airline userAirline = airlineService.findById(user.getAirlineId());

        if (hasNotes) {
            String jsonResponse = leonApiService.getAllFlightsByPeriod(userAirline, daysRange);
            List<FlightList> leonData = jsonMapper.readValue(jsonResponse, LeonMetaData.class)
                    .getData().getFlightList()
                    .stream()
                    .filter(flight -> flight.getAcft().getAcftType().getIsAircraft()
                            && flight.getAcft().getIsActive())
                    .filter(flight -> !flight.getIsCnl())
                    .filter((flight -> !flight.getNotes().getOps().isBlank()
                            || !flight.getNotes().getSales().isBlank()
                            || !flight.getTrip().getNotes().isBlank()
                            || flight.getTrip().getTripNotes() != null
                            && !flight.getTrip().getTripNotes()
                            .getTripSupplementaryInfo().isEmpty()))
                    .sorted(Comparator.comparing(FlightList::getStartTimeUtc))
                    .collect(toList());
            model.addAttribute("leonData", leonData);
            return "flights";
        }

        if (hasFiles) {
            String jsonResponse = leonApiService.getAllFlightsByPeriod(userAirline, daysRange);
            List<FlightList> leonData = jsonMapper.readValue(jsonResponse, LeonMetaData.class)
                    .getData().getFlightList()
                    .stream()
                    .filter(flight -> flight.getAcft().getAcftType().getIsAircraft()
                            && flight.getAcft().getIsActive())
                    .filter(flight -> flight.getChecklist().getAllItems()
                            .stream().anyMatch(files -> !files.getFiles().isEmpty()))
                    .sorted(Comparator.comparing(FlightList::getStartTimeUtc))
                    .collect(toList());
            model.addAttribute("leonData", leonData);
            return "flights";
        }

        if (registration != null) {
            Aircraft aircraft = aircraftService.findByRegistration(registration);
            String jsonResponse = leonApiService.getAllFlightsByPeriodAndAircraftId(
                    aircraft.getAirline(), daysRange, aircraft.getId());
            ArrayList<FlightList> leonData = jsonMapper.readValue(jsonResponse, LeonMetaData.class)
                    .getData().getFlightList();
            model.addAttribute("leonData", leonData);
        } else {
            String jsonResponse = leonApiService.getAllFlightsByPeriod(userAirline, daysRange);
            List<FlightList> leonData = jsonMapper.readValue(jsonResponse, LeonMetaData.class)
                    .getData().getFlightList()
                    .stream()
                    .filter(x -> !x.getIsCnl())
                    .filter(x -> x.getAcft().getAcftType().getIsAircraft())
                    .filter(x -> x.getAcft().getIsActive())
                    .collect(toList());
            model.addAttribute("leonData", leonData);
        }
        log.debug(String.format(
                "\"FLIGHTS\" page called for: %s withing daysRange: %s and Registration: %s",
                getVlzAirline().getName(), daysRange, registration));
        return "flights";
    }
}
