package planner.dao;

import java.util.List;
import java.util.Optional;
import planner.model.Airline;

public interface AirlineDao {
    Airline saveOrUpdate(Airline airline);

    Optional<Airline> findByName(String name);

    Optional<Airline> findByLeonDomain(String leonSubDomain);

    Optional<Airline> findById(Long id);

    List<Airline> findAll();

    void delete(Long id);
}
