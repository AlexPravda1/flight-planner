package planner.service.impl;

import static planner.util.LeonUtil.fixJsonForMapper;
import static planner.util.LeonUtil.validateJsonResponse;

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
        validateJsonResponse(response);
        return fixJsonForMapper(response);
    }

    @Override
    public String getAllAircraft(Airline airline) {
        String response = leonApiDao.getAllAircraft(airline);
        validateJsonResponse(response);
        return fixJsonForMapper(response);
    }
}
