package planner.config.template;

import lombok.extern.log4j.Log4j2;

@Log4j2
public enum EndpointConfig {
    LOGIN("/login"),
    USER_LOGIN("/user-login"),
    USER_AUTH("/auth"),
    REGISTER("/register"),
    INDEX("/index"),
    TEST("/test/**"),
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
