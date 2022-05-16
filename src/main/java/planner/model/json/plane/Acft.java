package planner.model.json.plane;

import lombok.Data;

@Data
public class Acft {
    private int aircraftNid;
    private String registration;
    private AcftType acftType;
}
