package planner.config.enums;

public enum UserRoleName {
    ADMIN("ADMIN"),
    USER("USER");

    private final String value;

    UserRoleName(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
