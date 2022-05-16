package planner.config.template.mapper.config;

public enum JsonMapperAircraftFieldsConfig {
    JSON_AIRCRAFT_ID("aircraftNid"),
    MODEL_AIRCRAFT_ID("id"),

    JSON_IS_AIRCRAFT("acftType.isAircraft"),
    MODEL_IS_AIRCRAFT("isAircraft"),

    JSON_AIRCRAFT_TYPE("acftType.icao"),
    MODEL_AIRCRAFT_TYPE("type"),

    JSON_AIRLINE_NAME("operator.name"),
    MODEL_AIRLINE_NAME("airline.name"),

    JSON_AIRLINE_ID("operator.oprNid"),
    MODEL_AIRLINE_ID("airline.id"),

    JSON_AIRLINE_LEON_SUBDOMAIN("operator.oprId"),
    MODEL_AIRLINE_LEON_SUBDOMAIN("airline.leonSubDomain"),

    JSON_IS_ACTIVE("isActive"),
    MODEL_IS_ACTIVE("isActive");

    private final String value;

    JsonMapperAircraftFieldsConfig(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
