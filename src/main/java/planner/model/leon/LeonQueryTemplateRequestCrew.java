package planner.model.leon;

public enum LeonQueryTemplateRequestCrew {
    CREW_LIST("crewList { position { occupation } contact { name, surname }} ");

    private final String value;

    LeonQueryTemplateRequestCrew(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
