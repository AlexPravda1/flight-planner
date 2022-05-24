package model.json;

public enum JsonSchemaConfig {
    AIRCRAFT_LIST_SCHEMA("classpath:jsonSchema/aircraft-list.json"),
    FLIGHT_LIST_SCHEMA("classpath:jsonSchema/flight-list.json");

    private final String value;

    JsonSchemaConfig(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
