package planner.service.impl;

import static java.util.stream.Collectors.toList;
import static planner.model.leon.LeonQueryTemplateRequestAircraft.AIRCRAFT_REGISTRATION_REGEX;

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
    public List<Aircraft> findAllActiveByAirline(String airlineName) {
        return aircraftDao.findAllActiveByAirline(airlineName).stream()
                .filter(acft -> acft.getRegistration().matches(AIRCRAFT_REGISTRATION_REGEX.value()))
                .collect(toList());
    }

    @Override
    public void delete(Long id) {
        aircraftDao.delete(id);
    }
}
