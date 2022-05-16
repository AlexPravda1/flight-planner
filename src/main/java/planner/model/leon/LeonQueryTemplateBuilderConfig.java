package planner.model.leon;

public enum LeonQueryTemplateBuilderConfig {
    QUERY_PREFIX("{\"query\":\"query { "),
    QUERY_POSTFIX("}\"} "),
    BODY_PREFIX("{ "),
    BODY_POSTFIX("} "),
    RESPONSE_UTC_CAPITAL("UTC"),
    RESPONSE_FIX_UTC_CAMEL_CASE("Utc"),
    ERROR("error");

    private final String value;

    LeonQueryTemplateBuilderConfig(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
