package planner.dao;

import planner.model.Airline;

public interface LeonApiDao {
    String getAllFlightsByPeriod(Airline airline, long daysRange);

    String getAllAircraft(Airline airline);
}
