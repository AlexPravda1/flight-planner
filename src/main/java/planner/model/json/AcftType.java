package planner.model.json;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
