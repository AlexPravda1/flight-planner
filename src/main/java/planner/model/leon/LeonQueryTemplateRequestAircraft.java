package planner.model.leon;

public enum LeonQueryTemplateRequestAircraft {
    AIRCRAFT_REGISTRATION_REGEX("[\\w]{1,2}[-][\\w]{3,5}"),
    AIRCRAFT("acft { isActive, aircraftNid, registration, acftType { icao, isAircraft }} ");

    private final String value;

    LeonQueryTemplateRequestAircraft(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
