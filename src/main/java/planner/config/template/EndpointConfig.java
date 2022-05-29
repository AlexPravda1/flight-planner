package planner.config.template;

public enum EndpointConfig {
    INDEX("/index/**"),
    TEST("/test/**"),
    LOGIN("/login/**"),
    REGISTER("/register/**"),
    FLIGHTS("/flights/**"),
    WELCOME("/welcome/**");

    private final String value;

    EndpointConfig(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
