package planner.config.enums;

public enum LeonQueryTemplate {

    TEMPLATE_PATH("src/main/resources/leon-query-templates/"),

    ALL_ACTIVE_AIRCRAFT("all-active-aircraft"),
    ALL_FLIGHTS_BY_PERIOD("all-flights-by-period"),
    TEST_QUERY_WILDCARD("test-query-wildcard");

    private final String value;

    LeonQueryTemplate(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
