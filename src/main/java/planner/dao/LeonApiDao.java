package planner.dao;

import planner.model.Airline;

public interface LeonApiDao {
    String getAllByPeriod(Airline airline, long daysRange);

    String getAllActiveAircraft(Airline airline);
}
