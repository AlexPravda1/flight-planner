package planner.dao.impl;

import static model.hardcoded.AirlineTest.getAirlineWithIdAndLeonApiKey;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static planner.util.LeonUtil.fixJsonForMappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.dozer.Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import planner.AbstractTest;
import planner.dao.LeonApiDao;
import planner.model.Aircraft;
import planner.model.Airline;
import planner.model.json.flight.list.FlightList;
import planner.model.json.plane.AircraftList;
import planner.model.json.root.LeonMetaData;

class LeonApiDaoImplTest extends AbstractTest {
    private static final Airline airline = getAirlineWithIdAndLeonApiKey();
    @Autowired private LeonApiDao leonApiDao;
    @Autowired private Mapper entityMapper;
    @Autowired private ObjectMapper jsonMapper;

    @Test
    void getAllAircraftJson_givenValidRequest_thenSuccess() {
        String jsonResponse = leonApiDao.getAllAircraft(airline);
        assertFalse(jsonResponse.contains("error"));
        assertTrue(jsonResponse.contains("registration"));
        assertTrue(jsonResponse.contains("isAircraft"));
        assertTrue(jsonResponse.contains("aircraftNid"));
    }

    @Test
    void getAllAircraftList_givenValidRequest_thenSuccess() throws JsonProcessingException {
        String jsonResponse = leonApiDao.getAllAircraft(airline);
        LeonMetaData leonData = jsonMapper.readValue(jsonResponse, LeonMetaData.class);
        List<Aircraft> activeAircraftList = getFilteredAircraftList(leonData);

        assertFalse(activeAircraftList.isEmpty());
        for (Aircraft aircraft : activeAircraftList) {
            assertTrue(aircraft.getIsAircraft());
            assertTrue(aircraft.getIsActive());
        }
    }

    @Test
    void getAllFlightsByPeriod_givenValidRequest_thenSuccess() throws JsonProcessingException {
        String jsonResponse = leonApiDao.getAllFlightsByPeriod(airline, 0L);
        LeonMetaData leonData = jsonMapper.readValue(
                fixJsonForMappers(jsonResponse), LeonMetaData.class);

        List<FlightList> commercialFlightList = getFilteredFlightList(leonData);
        validateFlightList(commercialFlightList);
    }

    @Test
    void getAllFlightsByPeriodAndAircraftId_validData_thenCorrect() throws JsonProcessingException {
        //log.info(leonApiDao.getAllFlightsByPeriodAndAircraftId(airline, 2L, 22249L));
        String jsonResponse = leonApiDao.getAllFlightsByPeriodAndAircraftId(airline, 0, 20611L);

        LeonMetaData leonData = jsonMapper.readValue(
                fixJsonForMappers(jsonResponse), LeonMetaData.class);
        List<FlightList> flightList = getFilteredFlightList(leonData);
        validateFlightList(flightList);
    }

    private void validateFlightList(List<FlightList> flightList) {
        assertFalse(flightList.isEmpty());
        for (FlightList flight : flightList) {
            assertTrue(flight.getIsCommercial());
            assertTrue(flight.getAcft().getIsActive());
            assertFalse(flight.getIsCnl());
        }
    }

    private List<Aircraft> getFilteredAircraftList(LeonMetaData leonData) {
        Predicate<AircraftList> onlyActiveAircraft = AircraftList::getIsActive;
        Predicate<AircraftList> onlyIsAircraft =
                aircraftList -> aircraftList.getAcftType().getIsAircraft();
        return leonData.getData().getAircraftList().stream()
                .filter(onlyActiveAircraft.and(onlyIsAircraft))
                .map(acft -> entityMapper.map(acft, Aircraft.class))
                .collect(Collectors.toList());
    }

    private List<FlightList> getFilteredFlightList(LeonMetaData leonData) {
        Predicate<FlightList> onlyCommercialFlight = FlightList::getIsCommercial;
        Predicate<FlightList> onlyActiveAircraft = flightList -> flightList.getAcft().getIsActive();
        return leonData.getData().getFlightList().stream()
                .filter(onlyCommercialFlight.and(onlyActiveAircraft))
                .collect(Collectors.toList());
    }
}
