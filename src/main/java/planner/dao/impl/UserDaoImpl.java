package planner.dao.impl;

import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import planner.dao.UserDao;
import planner.exception.DataProcessingException;
import planner.model.User;

@Repository
public class UserDaoImpl extends AbstractDao<User, Long> implements UserDao {
    private static final String MESSAGE = "Can't perform action on Entity: %S";

    public UserDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, User.class);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM User u JOIN FETCH u.roles WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResultOptional();
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "FROM User u JOIN FETCH u.roles", User.class).getResultList();
        } catch (Exception e) {
            throw new DataProcessingException(String.format(MESSAGE, "find all "
                    + clazz.getSimpleName()), e);
        }
    }
}
