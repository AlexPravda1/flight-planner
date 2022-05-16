package planner.config.template;

public enum MapperFieldsConfig {
    JSON_AIRCRAFT_ID("aircraftNid"),
    MODEL_AIRCRAFT_ID("id"),
    JSON_IS_AIRCRAFT("acftType.isAircraft"),
    MODEL_IS_AIRCRAFT("isAircraft"),
    JSON_AIRCRAFT_TYPE("acftType.icao"),
    MODE_AIRCRAFT_TYPE("type");

    private final String value;

    MapperFieldsConfig(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
