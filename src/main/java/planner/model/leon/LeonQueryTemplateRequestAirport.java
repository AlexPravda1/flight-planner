package planner.model.leon;

public enum LeonQueryTemplateRequestAirport {
    START_AIRPORT("startAirport { code { icao } city, country } "),
    END_AIRPORT("endAirport { code { icao } city, country } ");

    private final String value;

    LeonQueryTemplateRequestAirport(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
