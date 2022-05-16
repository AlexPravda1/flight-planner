package planner.service.impl;

import static planner.util.LeonUtil.validateJsonLeonResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import planner.dao.LeonApiDao;
import planner.model.Airline;
import planner.service.LeonApiService;

@Service
@RequiredArgsConstructor
public class LeonApiServiceImpl implements LeonApiService {
    private final LeonApiDao leonApiDao;

    @Override
    public String getAllFlightsByPeriod(Airline airline, long daysRange) {
        String response = leonApiDao.getAllFlightsByPeriod(airline, daysRange);
        validateJsonLeonResponse(response);
        return response;
    }

    @Override
    public String getAllActiveAircraft(Airline airline) {
        String response = leonApiDao.getAllAircraft(airline);
        validateJsonLeonResponse(response);
        return response;
    }

    @Override
    public String getAllFlightsByPeriodAndAircraftId(
            Airline airline, long daysRange, Long aircraftId) {
        String response =
                leonApiDao.getAllFlightsByPeriodAndAircraftId(airline, daysRange, aircraftId);
        validateJsonLeonResponse(response);
        return response;
    }
}
