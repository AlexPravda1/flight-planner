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
    //Limited by Leon reply "1400 max difficulty level" capability, gives "error" answer otherwise
    //Lower data requested from Leon increases max days amount possibility
    //80 days is limit determined "by hand" as of LeonQuery structure by 01-JUN-2022
    private static final int DAYS_RANGE = 80;
    private static Map<Long, List<FlightList>> allFlightsMap = new HashMap<>();
    private final LeonApiService leonApiService;
    private final ObjectMapper jsonMapper;

    public List<FlightList> getAllFlightsMap(Long airlineId) {
        log.info("Provided flights from MAP // no Leon Query " + LocalDateTime.now());
        return allFlightsMap.get(airlineId);
    }

    @Scheduled(fixedDelayString = "${cache.flight-list.renew.milliseconds}")
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
        log.info("(EVERY 15 MINs) Scheduled putAllFlights called to update FlightsMap at "
                + LocalDateTime.now());
    }
}
