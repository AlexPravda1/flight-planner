package planner.config.template;

public enum DataSourceBeanConfig {
    DB_DRIVER("db.driver"),
    DB_URL("db.url"),
    DB_USERNAME("db.username"),
    DB_PASSWORD("db.password");

    private final String value;

    DataSourceBeanConfig(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
