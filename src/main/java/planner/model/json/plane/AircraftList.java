package planner.model.json.plane;

import lombok.Data;
import planner.model.json.root.Operator;

@Data
public class AircraftList {
    private boolean isActive;
    private String registration;
    private int aircraftNid;
    private Operator operator;
    private AcftType acftType;

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
