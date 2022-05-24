package planner.dao.impl;

import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import planner.dao.AircraftDao;
import planner.exception.DataProcessingException;
import planner.model.Aircraft;

@Repository
public class AircraftDaoImpl extends AbstractDao<Aircraft, Long> implements AircraftDao {
    @Autowired
    protected AircraftDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Aircraft.class);
    }

    @Override
    public Optional<Aircraft> findByRegistration(String registration) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "FROM Aircraft a WHERE a.registration = :registration AND a.isActive = true",
                            Aircraft.class)
                    .setParameter("registration", registration)
                    .uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Couldn't get Aircraft by registration: "
                    + registration, e);
        }
    }

    @Override
    public List<Aircraft> findAllActiveByAirline(String airlineName) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Aircraft a WHERE a.airline.name = :airlineName "
                                    + "AND a.isActive = true", Aircraft.class)
                    .setParameter("airlineName", airlineName)
                    .getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Couldn't get Aircraft list for: "
                    + airlineName, e);
        }
    }
}
