package planner.model.json.passenger;

import java.util.ArrayList;
import lombok.Data;

@Data
public class PassengerList {
    private int countExcludingAnimals;
    private ArrayList<PassengerContactList> passengerContactList;
}
