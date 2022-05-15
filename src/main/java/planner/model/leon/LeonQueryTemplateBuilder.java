package planner.model.leon;

public enum LeonQueryTemplateBuilder {
    QUERY_PREFIX("{\"query\":\"query { "),
    QUERY_POSTFIX("}\"} "),
    BODY_PREFIX("{ "),
    BODY_POSTFIX("} "),
    ERROR("error");

    private final String value;

    LeonQueryTemplateBuilder(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
