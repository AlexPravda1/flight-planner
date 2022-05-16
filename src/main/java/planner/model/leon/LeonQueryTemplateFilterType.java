package planner.model.leon;

public enum LeonQueryTemplateFilterType {
    FILTER_TYPE_AIRCRAFT_CL30_WILDCARD("aircraftTypeList (wildcard: \\\"CL30\\\") "),
    FILTER_TYPE_ALL_AIRCRAFT_ACTIVE_ONLY("aircraftList (onlyActive:true) "),
    FILTER_TYPE_ALL_AIRCRAFT("aircraftList "),
    FILTER_TYPE_FLIGHT_LIST("flightList ");

    private final String value;

    LeonQueryTemplateFilterType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
