package model;

import static planner.model.UserRoleName.USER;

import java.util.Set;
import planner.model.Role;
import planner.model.User;

public final class UserHardcoded {
    public static User getUserNoRolesNoId() {
        return getUserWithBasicFields();
    }

    public static User getUserWithUserRoleNoId() {
        User user = getUserWithBasicFields();
        user.setRoles(Set.of(new Role(USER)));
        return user;
    }

    private static User getUserWithBasicFields() {
        User user = new User();
        user.setEmail("user@gmail.com");
        user.setPassword("12345");
        user.setName("John");
        user.setSurname("Terris");
        return user;
    }
}
