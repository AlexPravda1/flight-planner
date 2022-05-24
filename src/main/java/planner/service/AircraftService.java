package planner.service;

import java.util.List;
import planner.model.Aircraft;

public interface AircraftService {
    Aircraft saveOrUpdate(Aircraft aircraft);

    Aircraft findByRegistration(String registration);

    Aircraft findById(Long id);

    List<Aircraft> findAllActive();

    List<Aircraft> findAllActiveByAirline(String airlineName);

    void delete(Long id);
}
