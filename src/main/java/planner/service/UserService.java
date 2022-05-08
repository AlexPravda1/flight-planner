package planner.service;

import java.util.List;
import java.util.Optional;
import planner.model.User;

public interface UserService {
    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    void delete(Long id);

    List<User> findAll();
}
