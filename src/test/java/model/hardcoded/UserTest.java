package model.hardcoded;

import planner.model.User;

public final class UserTest {
    public User getUserNoRolesNoId() {
        User user = new User();
        user.setEmail("user@gmail.com");
        user.setPassword("12345");
        user.setName("John");
        user.setSurname("Terris");
        return user;
    }
}
