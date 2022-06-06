package planner.service.impl;

import static java.util.stream.Collectors.toList;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import planner.dao.AircraftDao;
import planner.exception.DataProcessingException;
import planner.model.Aircraft;
import planner.service.AircraftService;

@Service
@RequiredArgsConstructor
public class AircraftServiceImpl implements AircraftService {
    private final AircraftDao aircraftDao;

    @Override
    public Aircraft saveOrUpdate(Aircraft aircraft) {
        return aircraftDao.saveOrUpdate(aircraft);
    }

    @Override
    public Aircraft findByRegistration(String registration) {
        return aircraftDao.findByRegistration(registration)
                .orElseThrow(() -> new DataProcessingException(
                "Aircraft with registration: " + registration + " was not found in DB."));
    }

    @Override
    public Aircraft findById(Long id) {
        return aircraftDao.findById(id).orElseThrow(() -> new DataProcessingException(
                "Aircraft with ID: " + id + " was not found in DB."));
    }

    @Override
    public List<Aircraft> findAllActive() {
        return aircraftDao.findAll().stream()
                .filter(Aircraft::getIsActive)
                .collect(toList());
    }

    @Override
    public List<Aircraft> findAllActiveByAirlineName(String airlineName) {
        return aircraftDao.findAllActiveByAirlineName(airlineName);
    }

    @Override
    public List<Aircraft> findAllActiveByAirlineId(Long airlineId) {
        return aircraftDao.findAllActiveByAirlineId(airlineId);
    }

    @Override
    public void delete(Long id) {
        aircraftDao.delete(id);
    }
}
