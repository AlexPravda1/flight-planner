package planner.config.template.mapper.config;

public enum MapperAircraftResponseDtoFieldsConfig {
    AIRCRAFT_AIRLINE_NAME("airline.name"),
    AIRCRAFT_RESPONSE_DTO_AIRLINE_NAME("airlineName");

    private final String value;

    MapperAircraftResponseDtoFieldsConfig(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
