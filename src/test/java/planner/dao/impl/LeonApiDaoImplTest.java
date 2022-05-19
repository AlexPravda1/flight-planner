package planner.dao.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dozer.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import planner.AbstractTest;
import planner.dao.LeonApiDao;
import planner.model.Aircraft;
import planner.model.Airline;
import planner.model.json.flight.list.FlightList;
import planner.model.json.plane.AircraftList;
import planner.model.json.root.Root;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static planner.model.leon.LeonQueryTemplateBuilderConfig.RESPONSE_FIX_UTC_CAMEL_CASE;
import static planner.model.leon.LeonQueryTemplateBuilderConfig.RESPONSE_UTC_CAPITAL;

class LeonApiDaoImplTest extends AbstractTest {
    private Airline airline;
    @Autowired
    private LeonApiDao leonApiDao;
    @Autowired
    private Mapper entityMapper;
    @Autowired
    private ObjectMapper jsonMapper;

    @BeforeEach
    void setUp() {
        airline = new Airline();
        airline.setId(1L);
        airline.setName("Volare Aviation");
        airline.setLeonSubDomain("vlz");
        airline.setLeonApiKey(
                "ff0bd02c05f2d6916deeb8b9d022e03a388a7e0f48fb02e577faf41350d73cb32d47f6b0");
    }

    @Test
    void getAllAircraft_validData_thenCorrect() throws JsonProcessingException {
        String jsonResponse = leonApiDao.getAllAircraft(airline);
        Root root = jsonMapper.readValue(jsonResponse, Root.class);

        List<Aircraft> allActiveAicraft = root.getData().getAircraftList().stream()
                .filter(AircraftList::getIsActive)
                .filter(acft -> acft.getAcftType().getIsAircraft())
                .map(acft -> entityMapper.map(acft, Aircraft.class))
                .collect(Collectors.toList());

        assertNotEquals(allActiveAicraft, Collections.EMPTY_LIST);

        for (Aircraft aircraft : allActiveAicraft) {
            assertTrue(aircraft.getIsActive());
            assertEquals(Aircraft.class, aircraft.getClass());
        }

        List<Aircraft> allInactiveAicraft = root.getData().getAircraftList().stream()
                .filter(acft -> !acft.getIsActive())
                .filter(acft -> acft.getAcftType().getIsAircraft())
                .map(x -> entityMapper.map(x, Aircraft.class))
                .collect(Collectors.toList());

        assertNotEquals(allInactiveAicraft, Collections.EMPTY_LIST);
        for (Aircraft aircraft : allInactiveAicraft) {
            assertFalse(aircraft.getIsActive());
            assertTrue(aircraft.getIsAircraft());
        }
    }

    @Test
    @Disabled("To be implemented")
    void getAllFlightsByPeriod_validData_thenCorrect() {
        //PH
        assertTrue(true);
    }

    @Test
    void getAllFlightsByPeriodAndAircraftId_validData_thenCorrect() throws JsonProcessingException {
        //log.info(leonApiDao.getAllFlightsByPeriodAndAircraftId(airline, 2L, 22249L));
        String jsonResponse = leonApiDao.getAllFlightsByPeriodAndAircraftId(airline, 0, 17038L);

        // Temporary! Mappers should be tested separately?
        // "Replace" implemented on Service layer
        String jsonResponseFix = jsonResponse.replaceAll(RESPONSE_UTC_CAPITAL.value(),
                RESPONSE_FIX_UTC_CAMEL_CASE.value());

        Root root = jsonMapper.readValue(jsonResponseFix, Root.class);
        List<FlightList> flightList = root.getData().getFlightList().stream()
                .filter(flight -> !flight.getIsCnl())
                .filter(FlightList::getIsCommercial)
                .collect(Collectors.toList());

        assertNotEquals(flightList, Collections.EMPTY_LIST);
        for (FlightList flight : flightList) {
            assertFalse(flight.getIsCnl());
            assertTrue(flight.getIsCommercial());
        }
    }
}
