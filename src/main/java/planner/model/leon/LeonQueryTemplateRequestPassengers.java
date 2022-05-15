package planner.model.leon;

public enum LeonQueryTemplateRequestPassengers {
    PAX_COUNT("passengerList { countExcludingAnimals } "),
    PAX_CONTACTS("passengerList { passengerContactList { contact { name, surname }}} ");

    private final String value;

    LeonQueryTemplateRequestPassengers(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
