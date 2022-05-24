package planner.controller;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dozer.Mapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import planner.model.Aircraft;
import planner.model.Airline;
import planner.model.User;
import planner.model.json.plane.AircraftList;
import planner.model.json.root.LeonMetaData;
import planner.service.LeonApiService;
import planner.service.UserService;

@Controller
@RequiredArgsConstructor
@Log4j2
public class TestJspController {
    private final UserService userService;
    private final LeonApiService leonApiService;
    private final ObjectMapper jsonMapper;
    private final Mapper entityMapper;

    @GetMapping("/welcome")
    public String welcome(
            @RequestParam(name = "name", required = false, defaultValue = "Traveller")
            String name, Model model) throws JsonProcessingException {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("userName", name);

        LocalDateTime time = LocalDateTime.now();
        model.addAttribute("time", time);

        String jsonResponse = leonApiService.getAllAircraft(getAirline());
        LeonMetaData leonData = jsonMapper.readValue(jsonResponse, LeonMetaData.class);
        List<Aircraft> activeAircraftList = getFilteredAircraftList(leonData);
        model.addAttribute("acftList", activeAircraftList);
        log.info("processing welcome page");
        return "welcome";
    }

    private Airline getAirline() {
        Airline airline = new Airline();
        airline.setId(1L);
        airline.setName("Volare Aviation");
        airline.setLeonSubDomain("vlz");
        airline.setLeonApiKey(
                "ff0bd02c05f2d6916deeb8b9d022e03a388a7e0f48fb02e577faf41350d73cb32d47f6b0");
        return airline;
    }

    private List<Aircraft> getFilteredAircraftList(LeonMetaData leonData) {
        Predicate<AircraftList> onlyActiveAircraft = AircraftList::getIsActive;
        Predicate<AircraftList> onlyIsAircraft =
                aircraftList -> aircraftList.getAcftType().getIsAircraft();
        return leonData.getData().getAircraftList().stream()
                .filter(onlyActiveAircraft.and(onlyIsAircraft))
                .map(acft -> entityMapper.map(acft, Aircraft.class))
                .collect(toList());
    }
}
