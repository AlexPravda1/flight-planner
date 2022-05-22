package planner.model.json.flight.list;

import java.util.ArrayList;
import java.util.Date;
import lombok.Data;
import planner.model.json.airport.EndAirport;
import planner.model.json.airport.StartAirport;
import planner.model.json.checklist.Checklist;
import planner.model.json.crew.CrewList;
import planner.model.json.flight.watch.FlightWatch;
import planner.model.json.handling.LegHandling;
import planner.model.json.notes.Notes;
import planner.model.json.notes.Trip;
import planner.model.json.passenger.PassengerList;
import planner.model.json.plane.Acft;

@Data
public class FlightList {
    private boolean isCnl;
    private String icaoType;
    private boolean isCommercial;
    private String flightNo;
    private String flightType;
    private Date startTimeUtc;
    private Date endTimeUtc;
    private FlightWatch flightWatch;
    private Acft acft;
    private StartAirport startAirport;
    private EndAirport endAirport;
    private LegHandling legHandling;
    private Checklist checklist;
    private Notes notes;
    private Trip trip;
    private PassengerList passengerList;
    private ArrayList<CrewList> crewList;

    public void setIsCnl(boolean cnl) {
        isCnl = cnl;
    }

    public void setIsCommercial(boolean commercial) {
        isCommercial = commercial;
    }

    public boolean getIsCnl() {
        return isCnl;
    }

    public boolean getIsCommercial() {
        return isCommercial;
    }
}
