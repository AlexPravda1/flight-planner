package planner.service;

import java.util.List;
import planner.model.Aircraft;

public interface AircraftService {
    Aircraft saveOrUpdate(Aircraft aircraft);

    Aircraft findByRegistration(String registration);

    Aircraft findById(Long id);

    List<Aircraft> findAll();

    void delete(Long id);
}
