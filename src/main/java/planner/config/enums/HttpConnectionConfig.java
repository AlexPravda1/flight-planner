package planner.config.enums;

public enum HttpConnectionConfig {
    CONTENT_TYPE("Content-Type"),
    APPLICATION_JSON("application/json"),
    AUTHORIZATION("Authorization"),
    BEARER("Bearer "),
    HTTP_PREFIX("https://"),
    HTTP_POST("POST");

    private final String value;

    HttpConnectionConfig(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
