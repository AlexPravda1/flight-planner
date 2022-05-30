package planner.config.template;

import lombok.extern.log4j.Log4j2;

@Log4j2
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
        log.debug("Providing ENUM endpoint value " + value);
        return value;
    }
}
