package planner.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import planner.dao.LeonApiDao;
import planner.model.Airline;
import planner.service.LeonApiService;
import planner.util.LeonUtil;

@Service
@RequiredArgsConstructor
public class LeonApiServiceImpl implements LeonApiService {
    private final LeonApiDao leonApiDao;

    @Override
    public String getAllFlightsByPeriod(Airline airline, long daysRange) {
        String response = leonApiDao.getAllFlightsByPeriod(airline, daysRange);
        return LeonUtil.getValidatedResponse(response);
    }

    @Override
    public String getAllActiveAircraft(Airline airline) {
        String response = leonApiDao.getAllAircraft(airline);
        return LeonUtil.getValidatedResponse(response);
    }

    @Override
    public String getAllFlightsByPeriodAndAircraftId(
            Airline airline, long daysRange, Long aircraftId) {
        String response =
                leonApiDao.getAllFlightsByPeriodAndAircraftId(airline, daysRange, aircraftId);
        return LeonUtil.getValidatedResponse(response);
    }
}
