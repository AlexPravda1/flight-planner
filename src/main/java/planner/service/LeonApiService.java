package planner.service;

import planner.model.Airline;

public interface LeonApiService {
    String getAllFlightsByPeriod(Airline airline, long daysRange);

    String getAllActiveAircraft(Airline airline);

    String getAllFlightsByPeriodAndAircraftId(Airline airline, long daysRange, Long aircraftId);
}
