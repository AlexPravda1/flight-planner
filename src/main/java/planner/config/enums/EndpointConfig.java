package planner.config.enums;

public enum EndpointConfig {
    INDEX("/index/**"),
    TEST("/test/**"),
    LOGIN("/login/**"),
    REGISTER("/register/**");

    private final String value;

    EndpointConfig(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
