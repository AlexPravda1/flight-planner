package planner.service.impl;

import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;
import planner.model.json.flight.list.FlightList;
import planner.service.LeonFlightListService;

@Service
public class LeonFlightServiceImpl implements LeonFlightListService {
    @Override
    public List<FlightList> getFlightsHasNotes(List<FlightList> flights, boolean hasNotes) {
        return !hasNotes ? flights
                : flights.stream()
                .filter((flight -> !flight.getNotes().getOps().isBlank()
                        || !flight.getNotes().getSales().isBlank()
                        || !flight.getTrip().getNotes().isBlank()
                        || flight.getTrip().getTripNotes() != null
                        && !flight.getTrip().getTripNotes()
                        .getTripSupplementaryInfo().isEmpty()))
                .collect(toList());
    }

    @Override
    public List<FlightList> getFlightsHasRegistration(List<FlightList> flights,
                                                      String registration) {
        return registration == null ? flights
                : flights.stream()
                .filter(acft -> acft.getAcft().getRegistration().equals(registration))
                .collect(toList());
    }

    @Override
    public List<FlightList> getFlightsHasFiles(List<FlightList> flights, boolean hasFiles) {
        return !hasFiles ? flights
                : flights.stream()
                .filter(flight -> flight.getChecklist().getAllItems()
                        .stream().anyMatch(files -> !files.getFiles().isEmpty()))
                .collect(toList());
    }

    @Override
    public List<FlightList> getFlightsHasDaysRange(List<FlightList> flights, long daysRange) {
        LocalDateTime currentDatePlusLimit = LocalDate.now().atStartOfDay().plusDays(daysRange);
        Date dateLimit = Date.from(currentDatePlusLimit.atZone(ZoneOffset.UTC).toInstant());
        return daysRange == 0 ? flights
                : flights.stream()
                .filter(flight -> flight.getStartTimeUtc().before(dateLimit))
                .collect(toList());
    }
}
