package planner.model.leon;

public enum LeonQueryTemplate {

    QUERY_PREFIX("{\"query\":\"query { "),
    QUERY_POSTFIX("}\"} "),
    BODY_PREFIX("{ "),
    BODY_POSTFIX("} "),
    ERROR("error"),


    FILTER_AIRCRAFT_CL30_WILDCARD("aircraftTypeList (wildcard: \\\"CL30\\\") "),
    FILTER_ALL_ACTIVE_AIRCRAFT("aircraftList (onlyActive:true) "),
    FILTER_FLIGHT_LIST("flightList "),

    FILTER_CONDITIONS_FLIGHT_LIST_ALL_AIRCRAFT("(filter: {timeInterval: "
            + "{start: \\\"START_DATE\\\" "
            + "end: \\\"END_DATE\\\"} "
            + "flightStatus: CONFIRMED}) "),

    GENERAL_CONDITIONS_ALL("isCnl, icaoType, isCommercial, flightNo, "
            + "flightType, startTimeUTC, endTimeUTC "),

    GENERAL_CONDITIONS_ALL_AIRCRAFT_LIST("isActive, aircraftNid, registration, "
            + "acftType { acftTypeId, icao, isAircraft } "),

    FLIGHT_WATCH("flightWatch{ atd, ata, paxCount } "),

    AIRCRAFT("acft { aircraftNid, registration, acftType { icao, isAircraft }} "),

    START_AIRPORT("startAirport { code { icao } city, country } "),

    END_AIRPORT("endAirport { code { icao } city, country } "),

    SECTOR_HANDLING("legHandling {adepHandler { name, email, telephone, telephoneAfterHours }"
            + " adesHandler { name, email, telephone, telephoneAfterHours }} "),

    CHECKLIST_ALL("checklist {allItems "
            + "{ definition { label, section } "
            + "status { status, caption, color } "
            + "comment "
            + "files {fileName, signedUrl (expireMinutes: 30)}}} "),

    PAX_COUNT("passengerList { countExcludingAnimals } "),

    PAX_CONTACTS("passengerList { passengerContactList { contact { name, surname }}} "),

    CREW("crewList { position { occupation } contact { name, surname }} ");

    private final String value;

    LeonQueryTemplate(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
