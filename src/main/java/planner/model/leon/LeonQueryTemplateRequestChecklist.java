package planner.model.leon;

public enum LeonQueryTemplateRequestChecklist {
    CHECKLIST_ALL("checklist {allItems "
            + "{ definition { label, section } "
            + "status { status, caption, color } "
            + "comment "
            + "files {fileName, signedUrl (expireMinutes: 30)}}} ");

    private final String value;

    LeonQueryTemplateRequestChecklist(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
