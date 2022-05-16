package planner.model.json.root;

import java.util.ArrayList;
import planner.model.json.flight.list.FlightList;
import planner.model.json.plane.AircraftList;

@lombok.Data
public class Data {
    private ArrayList<AircraftList> aircraftList;
    private ArrayList<FlightList> flightList;
}
