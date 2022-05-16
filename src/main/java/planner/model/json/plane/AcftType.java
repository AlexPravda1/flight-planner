package planner.model.json.plane;

import lombok.Data;

@Data
public class AcftType {
    private String acftTypeId;
    private String icao;
    private boolean isAircraft;

    public void setIsAircraft(boolean isAircraft) {
        this.isAircraft = isAircraft;
    }

    public boolean getIsAircraft() {
        return isAircraft;
    }
}
