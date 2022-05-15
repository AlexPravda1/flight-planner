package planner.model.json;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AircraftList {
    private boolean isActive;
    private int aircraftNid;
    private String registration;
    private AcftType acftType;

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
