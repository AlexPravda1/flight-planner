package planner.model.json.plane;

import lombok.Data;

@Data
public class Acft {
    private boolean isActive;
    private int aircraftNid;
    private String registration;
    private AcftType acftType;

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }
}
