package planner.service;

import java.util.List;
import planner.model.Airline;

public interface AirlineService {
    Airline saveOrUpdate(Airline airline);

    Airline findByName(String name);

    Airline findByLeonDomain(String leonSubDomain);

    Airline findById(Long id);

    List<Airline> findAll();

    void delete(Long id);
}
