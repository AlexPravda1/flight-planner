package planner.config.template;

public enum WebJspConfig {
    WEB_INF("/WEB-INF/jsp/"),
    PAGE_SUFFIX(".jsp");

    private final String value;

    WebJspConfig(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
