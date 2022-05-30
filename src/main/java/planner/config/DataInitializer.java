package planner.config;

import static java.util.stream.Collectors.toList;
import static planner.model.UserRoleName.ADMIN;
import static planner.model.UserRoleName.USER;
import static planner.util.AirlineUtil.getVlzAirline;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dozer.Mapper;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import planner.dao.AircraftDao;
import planner.model.Aircraft;
import planner.model.Airline;
import planner.model.Role;
import planner.model.User;
import planner.model.json.plane.AircraftList;
import planner.model.json.root.LeonMetaData;
import planner.service.AirlineService;
import planner.service.LeonApiService;
import planner.service.RoleService;
import planner.service.UserService;

@Component
@RequiredArgsConstructor
@Log4j2
public class DataInitializer {
    protected final SessionFactory sessionFactory;
    private final RoleService roleService;
    private final UserService userService;
    private final LeonApiService leonApiService;
    private final AirlineService airlineService;
    private final AircraftDao aircraftDao;
    private final ObjectMapper jsonMapper;
    private final Mapper entityMapper;

    @PostConstruct
    public void injectUsers() {
        List<Role> roles = roleService.findAll();

        if (roles.isEmpty()) {
            roleService.save(new Role(ADMIN));
            roleService.save(new Role(USER));
            roles = roleService.findAll();

            User bobAdmin = new User();
            bobAdmin.setRoles(new HashSet<>(roles));
            bobAdmin.setEmail("bob@i.ua");
            bobAdmin.setPassword("1234");
            bobAdmin.setName("bob");
            bobAdmin.setSurname("bobinsky");
            userService.save(bobAdmin);

            User aliceUser = new User();
            aliceUser.setRoles(Set.of(roles.get(1)));
            aliceUser.setEmail("alice@i.ua");
            aliceUser.setPassword("1234");
            aliceUser.setName("alice");
            aliceUser.setSurname("alicynsky");
            userService.save(aliceUser);
            log.debug(String.format("Users injected: %s (ROLES: %S), %s (ROLES: %S)",
                    bobAdmin.getName(), bobAdmin.getRoles().toString(),
                    aliceUser.getName(), aliceUser.getRoles().toString()));
        }
    }

    @PostConstruct
    public void injectAirlineAndAircraft() throws JsonProcessingException {
        Airline airline = airlineService.saveOrUpdate(getVlzAirline());
        log.debug(airline.getName() + " Airline saved to internal DB");

        String jsonResponse = leonApiService.getAllAircraft(getVlzAirline());
        LeonMetaData leonData = jsonMapper.readValue(jsonResponse, LeonMetaData.class);
        List<Aircraft> aircraftList = getFilteredOnlyAircraftList(leonData);
        for (Aircraft aircraft : aircraftList) {
            aircraftDao.saveOrUpdate(aircraft);
            log.debug(aircraft.getRegistration() + " aircraft saved to internal DB");
        }
    }

    private List<Aircraft> getFilteredOnlyAircraftList(LeonMetaData leonData) {
        Predicate<AircraftList> onlyIsAircraft =
                aircraftList -> aircraftList.getAcftType().getIsAircraft();
        return leonData.getData().getAircraftList().stream()
                .filter(onlyIsAircraft)
                .map(acft -> entityMapper.map(acft, Aircraft.class))
                .collect(toList());
    }
}
