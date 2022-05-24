package planner.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import planner.dao.AirlineDao;
import planner.exception.DataProcessingException;
import planner.model.Airline;
import planner.service.AirlineService;

@Service
@RequiredArgsConstructor
public class AirlineServiceImpl implements AirlineService {
    private final AirlineDao airlineDao;

    @Override
    public Airline saveOrUpdate(Airline airline) {
        return airlineDao.saveOrUpdate(airline);
    }

    @Override
    public Airline findByName(String name) {
        return airlineDao.findByName(name).orElseThrow(() -> new DataProcessingException(
                "Airline with name: " + name + " was not found in DB."));
    }

    @Override
    public Airline findById(Long id) {
        return airlineDao.findById(id).orElseThrow(() -> new DataProcessingException(
                "Airline with id: " + id + " was not found in DB."));
    }

    @Override
    public List<Airline> findAll() {
        return airlineDao.findAll();
    }

    @Override
    public void delete(Long id) {
        airlineDao.delete(id);
    }
}
