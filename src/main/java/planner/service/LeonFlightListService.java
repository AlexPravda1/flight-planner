package planner.service;

import java.util.List;
import planner.model.json.flight.list.FlightList;

public interface LeonFlightListService {
    List<FlightList> getFlightsHasNotes(List<FlightList> flights, boolean hasNotes);

    List<FlightList> getFlightsHasRegistration(List<FlightList> flights, String registration);

    List<FlightList> getFlightsHasFiles(List<FlightList> flights, boolean hasFiles);

    List<FlightList> getFlightsHasDaysRange(List<FlightList> flights, long daysRange);
}
