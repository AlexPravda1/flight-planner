package planner.config.template;

public enum WebSecurityConfig {
    ANT_RESOURCES("/resources/**"),
    ANT_STATIC("/static/**"),
    ANT_CSS("/css/**"),
    ANT_JS("/js/**"),
    ANT_IMAGES("/images/**"),

    PATH_RESOURCES("/resources/");

    private final String value;

    WebSecurityConfig(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
