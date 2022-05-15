package planner.model.leon;

public enum LeonQueryTemplateRequestFlightTimings {
    FLIGHT_WATCH("flightWatch{ atd, ata, paxCount } ");

    private final String value;

    LeonQueryTemplateRequestFlightTimings(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
