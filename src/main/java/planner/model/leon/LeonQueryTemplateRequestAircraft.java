package planner.model.leon;

public enum LeonQueryTemplateRequestAircraft {
    AIRCRAFT("acft { aircraftNid, registration, acftType { icao, isAircraft }} ");

    private final String value;

    LeonQueryTemplateRequestAircraft(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
