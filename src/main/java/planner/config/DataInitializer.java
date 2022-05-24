package planner.config;

import static planner.model.UserRoleName.ADMIN;
import static planner.model.UserRoleName.USER;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dozer.Mapper;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import planner.dao.AircraftDao;
import planner.model.Role;
import planner.model.User;
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
        }
    }
    /*
    @PostConstruct
    public void injectAircraft() throws JsonProcessingException {
        String jsonResponse = leonApiService.getAllAircraft(getAirline());
        LeonMetaData leonData = jsonMapper.readValue(jsonResponse, LeonMetaData.class);
        List<Aircraft> activeAircraftList = getFilteredAircraftList(leonData);

        for (Aircraft aircraft : activeAircraftList) {
            Session session = null;
            Transaction transaction = null;
            try {
                session = sessionFactory.openSession();
                transaction = session.beginTransaction();
                session.saveOrUpdate(aircraft);
                transaction.commit();
                log.info("Saved to DB aircraft: " + aircraft.getRegistration());
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw new DataProcessingException("Cant save " + aircraft, e);
            } finally {
                if (session != null) {
                    session.close();
                }
            }
        }
    }

    private Airline getAirline() {
        Airline airline = new Airline();
        airline.setId(1063L);
        airline.setName("Volare Aviation");
        airline.setLeonSubDomain("vlz");
        airline.setLeonApiKey(
                "ff0bd02c05f2d6916deeb8b9d022e03a388a7e0f48fb02e577faf41350d73cb32d47f6b0");

        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.saveOrUpdate(airline);
            transaction.commit();
            log.info("Saved to DB Airline: " + airline.getName());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Cant save " + airline, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return airline;
    }



 */

    /*
    @PostConstruct
    public void injectAirline() {
        Airline airline = airlineService.saveOrUpdate(getVlzAirline());
        log.debug(airline.getName() + " Airline saved to internal DB");
    }

    @PostConstruct
    public void injectAircraft() throws JsonProcessingException {
        String jsonResponse = leonApiService.getAllAircraft(getVlzAirline());
        LeonMetaData leonData = jsonMapper.readValue(jsonResponse, LeonMetaData.class);
        List<Aircraft> aircraftList = getFilteredAircraftOnlyList(leonData);
        for (Aircraft aircraft : aircraftList) {
            aircraftDao.saveOrUpdate(aircraft);
            log.debug(aircraft.getRegistration() + " aircraft saved to internal DB");
        }
    }

    private List<Aircraft> getFilteredAircraftOnlyList(LeonMetaData leonData) {
        Predicate<AircraftList> onlyIsAircraft =
                aircraftList -> aircraftList.getAcftType().getIsAircraft();
        return leonData.getData().getAircraftList().stream()
                .filter(onlyIsAircraft)
                .map(acft -> entityMapper.map(acft, Aircraft.class))
                .collect(toList());
    }

     */
}
