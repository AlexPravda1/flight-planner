package planner.config;

import static java.util.stream.Collectors.toList;
import static planner.model.UserRoleName.ADMIN;
import static planner.model.UserRoleName.USER;

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
import planner.util.AirlineUtil;

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

            User superAdmin = new User();
            superAdmin.setRoles(new HashSet<>(roles));
            superAdmin.setEmail("admin@i.ua");
            superAdmin.setPassword("SuperPassword123!");
            superAdmin.setName("Admin");
            superAdmin.setSurname("Adminsky");
            superAdmin.setAirlineId(1063L);
            userService.save(superAdmin);

            User johnUser = new User();
            johnUser.setRoles(Set.of(roles.get(1)));
            johnUser.setEmail("volare@jbs.aero");
            johnUser.setPassword("John1234!");
            johnUser.setName("John");
            johnUser.setSurname("Braid");
            johnUser.setAirlineId(1063L);
            userService.save(johnUser);

            User jbsUser = new User();
            jbsUser.setRoles(Set.of(roles.get(1)));
            jbsUser.setEmail("ops@jbs.aero");
            jbsUser.setPassword("JBSOperations1");
            jbsUser.setName("JBS Operations");
            jbsUser.setSurname("JBS");
            jbsUser.setAirlineId(1063L);
            userService.save(jbsUser);
            log.debug("Users injected: {}, {}, {}",
                    superAdmin.getName(), johnUser.getName(), jbsUser.getName());
        }
    }

    @PostConstruct
    public void injectAirlineAndAircraft() throws JsonProcessingException {
        for (Airline airline : AirlineUtil.getAllAirlines()) {
            airline = airlineService.saveOrUpdate(airline);
            log.debug(airline.getName() + " Airline saved to internal DB");

            String jsonResponse = leonApiService.getAllAircraft(airline);
            LeonMetaData leonData = jsonMapper.readValue(jsonResponse, LeonMetaData.class);
            List<Aircraft> aircraftList = getFilteredOnlyAircraftList(leonData);
            for (Aircraft aircraft : aircraftList) {
                aircraftDao.saveOrUpdate(aircraft);
                log.debug(aircraft.getRegistration() + " aircraft saved to internal DB");
            }
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
