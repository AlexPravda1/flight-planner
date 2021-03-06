package planner.dao;

import java.util.List;
import java.util.Optional;
import planner.model.Aircraft;

public interface AircraftDao {
    Aircraft saveOrUpdate(Aircraft aircraft);

    Optional<Aircraft> findByRegistration(String registration);

    Optional<Aircraft> findById(Long id);

    List<Aircraft> findAll();

    List<Aircraft> findAllActiveByAirlineName(String airlineName);

    void delete(Long id);

    List<Aircraft> findAllActiveByAirlineId(Long airlineId);
}
