package planner.dao;

import java.util.List;
import java.util.Optional;
import planner.model.User;

public interface UserDao {
    User save(User user);

    User saveOrUpdate(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    List<User> findAll();

    User update(User user);

    void delete(Long id);
}
