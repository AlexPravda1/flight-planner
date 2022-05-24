package planner.service.impl;

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
    public List<Aircraft> findAll() {
        return aircraftDao.findAll();
    }

    @Override
    public void delete(Long id) {
        aircraftDao.delete(id);
    }
}
