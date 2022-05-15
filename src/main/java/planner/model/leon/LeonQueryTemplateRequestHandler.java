package planner.model.leon;

public enum LeonQueryTemplateRequestHandler {
    SECTOR_HANDLING("legHandling {adepHandler { name, email, telephone, telephoneAfterHours }"
            + " adesHandler { name, email, telephone, telephoneAfterHours }} ");

    private final String value;

    LeonQueryTemplateRequestHandler(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
