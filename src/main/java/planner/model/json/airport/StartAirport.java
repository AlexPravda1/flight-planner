package planner.model.json.airport;

import lombok.Data;

@Data
public class StartAirport {
    private Code code;
    private String city;
    private String country;
}
