package planner.model.leon;

public enum LeonQueryTemplateFilterConditions {
    DEFAULT_DAYS_RANGE("10"),
    START_DAY("START_DAY"),
    END_DAY("END_DAY"),
    AIRCRAFT_ID("AIRCRAFT_ID"),

    GENERAL_CONDITIONS_ALL("isCnl, icaoType, isCommercial, flightNo, "
            + "flightType, startTimeUTC, endTimeUTC "),

    GENERAL_CONDITIONS_ALL_ACTIVE_AIRCRAFT_LIST("isActive, aircraftNid, registration, "
            + "acftType { acftTypeId, icao, isAircraft } "
            + "operator {name, oprId, oprNid}"),

    CONDITIONS_FLIGHT_LIST_ALL_AIRCRAFT("(filter: {timeInterval: "
            + "{start: \\\"" + START_DAY + "\\\" "
            + "end: \\\"" + END_DAY + "\\\"} "
            + "flightStatus: CONFIRMED}) "),

    CONDITIONS_FLIGHT_LIST_BY_AIRCRAFT("(filter: {timeInterval: "
            + "{start: \\\"" + START_DAY + "\\\" "
            + "end: \\\"" + END_DAY + "\\\"} "
            + "aircraftNid: " + AIRCRAFT_ID
            + "flightStatus: CONFIRMED}) ");

    private final String value;

    LeonQueryTemplateFilterConditions(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
