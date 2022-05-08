package planner.dao.impl;

import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import planner.dao.UserDao;
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
                            "SELECT u FROM User u JOIN FETCH u.roles WHERE u.email = :email",
                            User.class)
                    .setParameter("email", email)
                    .uniqueResultOptional();
        }
    }
    /*  WHEN FETCHING LIKE BELOW GIVES 3 USERS? "BOB" is Duplicated for some reason
    @Override
    public List<User> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "SELECT u FROM User u JOIN FETCH u.roles", User.class).getResultList();
        } catch (Exception e) {
            throw new DataProcessingException(String.format(MESSAGE, "find all "
                    + clazz.getSimpleName()), e);
        }
    }*/
}
