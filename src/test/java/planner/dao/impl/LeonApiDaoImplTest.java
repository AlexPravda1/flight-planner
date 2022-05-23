package planner.dao.impl;

import static java.util.stream.Collectors.toList;
import static model.hardcoded.AirlineTest.getAirlineWithIdAndLeonApiKey;
import static model.hardcoded.json.JsonSchemaConfig.AIRCRAFT_LIST_SCHEMA;
import static model.hardcoded.json.JsonSchemaConfig.FLIGHT_LIST_SCHEMA;
import static org.apache.commons.io.FileUtils.openInputStream;
import static org.apache.commons.io.IOUtils.toInputStream;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.util.ResourceUtils.getFile;
import static planner.model.leon.LeonQueryTemplateBuilderConfig.ERROR;
import static planner.util.LeonUtil.fixJsonForMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
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
        validateJsonString(jsonResponse);
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
    void getAllFlightsJson_givenValidRequest_thenSuccess() {
        String jsonResponse = leonApiDao.getAllFlightsByPeriod(airline, 0L);
        validateJsonString(jsonResponse);
    }

    @Test
    void getAllFlightsByPeriod_givenValidRequest_thenSuccess() throws JsonProcessingException {
        String jsonResponse = leonApiDao.getAllFlightsByPeriod(airline, 0L);
        LeonMetaData leonData = jsonMapper.readValue(
                fixJsonForMapper(jsonResponse), LeonMetaData.class);
        List<FlightList> commercialFlightList = getFilteredFlightList(leonData);
        validateFlightList(commercialFlightList);
    }

    @Test
    void getAllFlightsByPeriodAndAircraftId_validData_thenCorrect() throws JsonProcessingException {
        String jsonResponse = leonApiDao.getAllFlightsByPeriodAndAircraftId(airline, 0L, 20611L);
        LeonMetaData leonData = jsonMapper.readValue(
                fixJsonForMapper(jsonResponse), LeonMetaData.class);
        List<FlightList> commercialFlightList = getFilteredFlightList(leonData);
        validateFlightList(commercialFlightList);
    }

    @Test
    void getAllFlightsByPeriodAndAircraftIdJson_givenValidRequest_thenSuccess() {
        String jsonResponse = leonApiDao.getAllFlightsByPeriodAndAircraftId(airline, 0L, 20611L);
        validateJsonString(jsonResponse);
    }

    @Test
    void validateJsonSchema_forAllAircraftList_givenValidJson_thenSuccess() throws IOException {
        String jsonResponse = leonApiDao.getAllAircraft(airline);
        assertTrue(getJsonSchemaValidationMessageSet(jsonResponse, AIRCRAFT_LIST_SCHEMA.value())
                .isEmpty());
    }

    @Test
    void validateJsonSchema_forAllFlightList_givenValidJson_thenSuccess() throws IOException {
        String jsonResponse = leonApiDao.getAllFlightsByPeriod(airline, 4L);
        assertTrue(getJsonSchemaValidationMessageSet(jsonResponse, FLIGHT_LIST_SCHEMA.value())
                .isEmpty());

        jsonResponse = leonApiDao.getAllFlightsByPeriod(airline, 10L);
        assertTrue(getJsonSchemaValidationMessageSet(jsonResponse, FLIGHT_LIST_SCHEMA.value())
                .isEmpty());

        jsonResponse = leonApiDao.getAllFlightsByPeriod(airline, 15L);
        assertTrue(getJsonSchemaValidationMessageSet(jsonResponse, FLIGHT_LIST_SCHEMA.value())
                .isEmpty());
    }

    @Test
    void validateJsonSchema_forAllFlightListByAircraftId_givenValidJson_thenSuccess()
            throws IOException {
        String jsonResponse = leonApiDao.getAllFlightsByPeriodAndAircraftId(airline, 0L, 22249L);
        assertTrue(getJsonSchemaValidationMessageSet(jsonResponse, FLIGHT_LIST_SCHEMA.value())
                .isEmpty());

        jsonResponse = leonApiDao.getAllFlightsByPeriodAndAircraftId(airline, 10L, 19413L);
        assertTrue(getJsonSchemaValidationMessageSet(jsonResponse, FLIGHT_LIST_SCHEMA.value())
                .isEmpty());

        jsonResponse = leonApiDao.getAllFlightsByPeriodAndAircraftId(airline, 20L, 19653L);
        assertTrue(getJsonSchemaValidationMessageSet(jsonResponse, FLIGHT_LIST_SCHEMA.value())
                .isEmpty());
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
                .collect(toList());
    }

    private List<FlightList> getFilteredFlightList(LeonMetaData leonData) {
        Predicate<FlightList> onlyCommercialFlight = FlightList::getIsCommercial;
        Predicate<FlightList> onlyActiveAircraft =
                flightList -> flightList.getAcft().getIsActive();
        return leonData.getData().getFlightList().stream()
                .filter(onlyCommercialFlight.and(onlyActiveAircraft))
                .collect(toList());
    }

    private void validateJsonString(String jsonResponse) {
        assertFalse(jsonResponse.contains(ERROR.value()));
        assertTrue(jsonResponse.contains("registration"));
        assertTrue(jsonResponse.contains("isAircraft"));
        assertTrue(jsonResponse.contains("aircraftNid"));
    }

    private Set<ValidationMessage> getJsonSchemaValidationMessageSet(
            String jsonLeonResponse, String schemaFilePath) throws IOException {
        try (
                InputStream jsonStream = toInputStream(jsonLeonResponse, StandardCharsets.UTF_8);
                InputStream schemaStream = openInputStream(getFile(schemaFilePath))
        ) {
            JsonNode json = jsonMapper.readTree(jsonStream);
            JsonSchema schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909)
                    .getSchema(schemaStream);
            return schema.validate(json);
        }
    }
}
