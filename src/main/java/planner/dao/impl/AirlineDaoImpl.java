package planner.dao.impl;

import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import planner.dao.AirlineDao;
import planner.exception.DataProcessingException;
import planner.model.Airline;

@Repository
public class AirlineDaoImpl extends AbstractDao<Airline, Long> implements AirlineDao {
    @Autowired
    protected AirlineDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Airline.class);
    }

    @Override
    public Optional<Airline> findByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Airline a WHERE a.name = :name", Airline.class)
                    .setParameter("name", name)
                    .uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Couldn't get Airline by name: " + name, e);
        }
    }

    @Override
    public Optional<Airline> findByLeonDomain(String leonSubDomain) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Airline a WHERE a.leonSubDomain = :leonSubDomain",
                            Airline.class)
                    .setParameter("leonSubDomain", leonSubDomain)
                    .uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Couldn't get Airline by name: " + leonSubDomain, e);
        }
    }
}
