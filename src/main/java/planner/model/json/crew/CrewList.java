package planner.model.json.crew;

import lombok.Data;
import planner.model.json.contact.Contact;

@Data
public class CrewList {
    private Position position;
    private Contact contact;
}
