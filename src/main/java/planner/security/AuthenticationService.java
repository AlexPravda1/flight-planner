package planner.security;

import planner.exception.AuthenticationException;
import planner.model.User;

public interface AuthenticationService {
    User register(String email, String password, String name, String surname);

    User login(String login, String password) throws AuthenticationException;
}
