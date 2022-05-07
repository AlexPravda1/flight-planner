package planner.config;

public enum ConfigProperty {
    DB_DRIVER("db.driver"),
    DB_URL("db.url"),
    DB_USERNAME("db.username"),
    DB_PASSWORD("db.password"),

    HIBERNATE_SQL("hibernate.show_sql"),
    HIBERNATE_DIALECT("hibernate.dialect"),
    HIBERNATE_HBM2DDL("hibernate.hbm2ddl.auto"),

    PACKAGES_TO_SCAN("planner.model"),

    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USER"),

    POINT_INDEX("/index"),
    POINT_TEST("/test/**"),
    POINT_LOGIN("/login/**"),
    POINT_REGISTER("/register/**");

    private final String value;

    ConfigProperty(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
