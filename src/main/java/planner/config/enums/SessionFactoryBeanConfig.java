package planner.config.enums;

public enum SessionFactoryBeanConfig {
    SHOW_SQL("hibernate.show_sql"),
    HIBERNATE_DIALECT("hibernate.dialect"),
    HIBERNATE_HBM2DDL("hibernate.hbm2ddl.auto"),
    PACKAGES_TO_SCAN("planner.model");

    private final String value;

    SessionFactoryBeanConfig(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
