package planner.controller;

import static java.util.stream.Collectors.toList;
import static planner.util.AirlineUtil.getVlzAirline;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dozer.Mapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import planner.model.Aircraft;
import planner.model.User;
import planner.model.dto.response.AircraftResponseDto;
import planner.model.json.flight.list.FlightList;
import planner.model.json.root.LeonMetaData;
import planner.service.AircraftService;
import planner.service.LeonApiService;
import planner.service.UserService;

@Controller
@RequiredArgsConstructor
@Log4j2
public class TestJspController {
    private final UserService userService;
    private final AircraftService aircraftService;
    private final LeonApiService leonApiService;
    private final Mapper entityMapper;
    private final ObjectMapper jsonMapper;

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

    @GetMapping("/flights")
    public String flights(
            @RequestParam(name = "registration", required = false) String registration,
            @RequestParam(name = "daysRange", required = false, defaultValue = "0") long daysRange,
            @RequestParam(name = "hasNotes", required = false, defaultValue = "false")
            boolean hasNotes,
            Model model) throws JsonProcessingException {

        if (hasNotes) {
            String jsonResponse = leonApiService.getAllFlightsByPeriod(getVlzAirline(), daysRange);
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

        if (registration != null) {
            Aircraft aircraft = aircraftService.findByRegistration(registration);
            String jsonResponse = leonApiService.getAllFlightsByPeriodAndAircraftId(
                    aircraft.getAirline(), daysRange, aircraft.getId());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy HH:mm");
            ArrayList<FlightList> leonData = jsonMapper.readValue(jsonResponse, LeonMetaData.class)
                    .getData().getFlightList();
            model.addAttribute("leonData", leonData);
        } else {
            //airline info must be taken from Auth session or User info
            String jsonResponse = leonApiService.getAllFlightsByPeriod(getVlzAirline(), daysRange);
            List<FlightList> leonData = jsonMapper.readValue(jsonResponse, LeonMetaData.class)
                    .getData().getFlightList()
                    .stream()
                    .filter(x -> !x.getIsCnl())
                    .collect(toList());
            model.addAttribute("leonData", leonData);
        }
        log.info("processing flights page");
        return "flights";
    }
}
