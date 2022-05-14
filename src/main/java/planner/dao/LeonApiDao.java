package planner.dao;

import planner.model.Airline;

public interface LeonApiDao {
    String getAllFlightsByPeriod(Airline airline, long daysRange);

    String getAllActiveAircraft(Airline airline);

    String getAllFlightsByPeriodAndAircraftId(Airline airline, long daysRange, Long aircraftId);
}
