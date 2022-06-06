package planner.util;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import planner.model.Airline;
import planner.model.json.flight.list.FlightList;
import planner.model.json.root.LeonMetaData;
import planner.service.LeonApiService;

@Component
@Log4j2
@RequiredArgsConstructor
public class LeonCacheUtil {
    //Limited by Leon reply "1400 max difficulty level" capability
    //Lower data amount transmitted from Leon increase max days amount possibility
    private static final int DAYS_RANGE = 80;
    private final LeonApiService leonApiService;
    private final ObjectMapper jsonMapper;
    private final Map<Long, List<FlightList>> allFlightsMap = new HashMap<>();

    public List<FlightList> getAllFlightsMap(Long airlineId) {
        log.info("Provided flights from MAP // no Leon Query " + LocalDateTime.now());
        return allFlightsMap.get(airlineId);
    }

    @Scheduled(fixedDelay = 1000 * 60 * 5)
    private void putAllFlights() throws JsonProcessingException {
        for (Airline airline : AirlineUtil.getAllAirlines()) {
            String jsonResponse = leonApiService.getAllFlightsByPeriod(airline, DAYS_RANGE);
            List<FlightList> leonData = jsonMapper.readValue(jsonResponse, LeonMetaData.class)
                    .getData().getFlightList()
                    .stream()
                    .filter(acft -> !acft.getIsCnl())
                    .filter(acft -> acft.getAcft().getAcftType().getIsAircraft()
                            && acft.getAcft().getIsActive())
                    .sorted(Comparator.comparing(FlightList::getStartTimeUtc))
                    .collect(toList());
            allFlightsMap.put(airline.getId(), leonData);
        }
        log.info("(EVERY 5 MINs) Scheduled putAllFlights called to update FlightsMap at "
                + LocalDateTime.now());
    }
}
